package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.Beneficiary;
import ae.gov.dubaipolice.happy.repository.BeneficiaryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BeneficiaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeneficiaryResourceIT {

    private static final String DEFAULT_EID = "AAAAAAAAAA";
    private static final String UPDATED_EID = "BBBBBBBBBB";

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME_EN = "BBBBBBBBBB";

    private static final Long DEFAULT_GRP_NUMBER = 1L;
    private static final Long UPDATED_GRP_NUMBER = 2L;

    private static final Long DEFAULT_NATIONALITY = 1L;
    private static final Long UPDATED_NATIONALITY = 2L;

    private static final Integer DEFAULT_GENDER_ID = 1;
    private static final Integer UPDATED_GENDER_ID = 2;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/beneficiaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiaryMockMvc;

    private Beneficiary beneficiary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createEntity(EntityManager em) {
        Beneficiary beneficiary = new Beneficiary()
            .eid(DEFAULT_EID)
            .uid(DEFAULT_UID)
            .fullName(DEFAULT_FULL_NAME)
            .fullNameEn(DEFAULT_FULL_NAME_EN)
            .grpNumber(DEFAULT_GRP_NUMBER)
            .nationality(DEFAULT_NATIONALITY)
            .genderId(DEFAULT_GENDER_ID)
            .birthDate(DEFAULT_BIRTH_DATE);
        return beneficiary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createUpdatedEntity(EntityManager em) {
        Beneficiary beneficiary = new Beneficiary()
            .eid(UPDATED_EID)
            .uid(UPDATED_UID)
            .fullName(UPDATED_FULL_NAME)
            .fullNameEn(UPDATED_FULL_NAME_EN)
            .grpNumber(UPDATED_GRP_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .genderId(UPDATED_GENDER_ID)
            .birthDate(UPDATED_BIRTH_DATE);
        return beneficiary;
    }

    @BeforeEach
    public void initTest() {
        beneficiary = createEntity(em);
    }

    @Test
    @Transactional
    void createBeneficiary() throws Exception {
        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();
        // Create the Beneficiary
        restBeneficiaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isCreated());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEid()).isEqualTo(DEFAULT_EID);
        assertThat(testBeneficiary.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testBeneficiary.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testBeneficiary.getFullNameEn()).isEqualTo(DEFAULT_FULL_NAME_EN);
        assertThat(testBeneficiary.getGrpNumber()).isEqualTo(DEFAULT_GRP_NUMBER);
        assertThat(testBeneficiary.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testBeneficiary.getGenderId()).isEqualTo(DEFAULT_GENDER_ID);
        assertThat(testBeneficiary.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void createBeneficiaryWithExistingId() throws Exception {
        // Create the Beneficiary with an existing ID
        beneficiary.setId(1L);

        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBeneficiaries() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get all the beneficiaryList
        restBeneficiaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].eid").value(hasItem(DEFAULT_EID)))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].fullNameEn").value(hasItem(DEFAULT_FULL_NAME_EN)))
            .andExpect(jsonPath("$.[*].grpNumber").value(hasItem(DEFAULT_GRP_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.intValue())))
            .andExpect(jsonPath("$.[*].genderId").value(hasItem(DEFAULT_GENDER_ID)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    void getBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get the beneficiary
        restBeneficiaryMockMvc
            .perform(get(ENTITY_API_URL_ID, beneficiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beneficiary.getId().intValue()))
            .andExpect(jsonPath("$.eid").value(DEFAULT_EID))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.fullNameEn").value(DEFAULT_FULL_NAME_EN))
            .andExpect(jsonPath("$.grpNumber").value(DEFAULT_GRP_NUMBER.intValue()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.intValue()))
            .andExpect(jsonPath("$.genderId").value(DEFAULT_GENDER_ID))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBeneficiary() throws Exception {
        // Get the beneficiary
        restBeneficiaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary
        Beneficiary updatedBeneficiary = beneficiaryRepository.findById(beneficiary.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiary are not directly saved in db
        em.detach(updatedBeneficiary);
        updatedBeneficiary
            .eid(UPDATED_EID)
            .uid(UPDATED_UID)
            .fullName(UPDATED_FULL_NAME)
            .fullNameEn(UPDATED_FULL_NAME_EN)
            .grpNumber(UPDATED_GRP_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .genderId(UPDATED_GENDER_ID)
            .birthDate(UPDATED_BIRTH_DATE);

        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBeneficiary.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBeneficiary))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testBeneficiary.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testBeneficiary.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testBeneficiary.getFullNameEn()).isEqualTo(UPDATED_FULL_NAME_EN);
        assertThat(testBeneficiary.getGrpNumber()).isEqualTo(UPDATED_GRP_NUMBER);
        assertThat(testBeneficiary.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testBeneficiary.getGenderId()).isEqualTo(UPDATED_GENDER_ID);
        assertThat(testBeneficiary.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiary.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeneficiaryWithPatch() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary using partial update
        Beneficiary partialUpdatedBeneficiary = new Beneficiary();
        partialUpdatedBeneficiary.setId(beneficiary.getId());

        partialUpdatedBeneficiary
            .eid(UPDATED_EID)
            .uid(UPDATED_UID)
            .fullName(UPDATED_FULL_NAME)
            .fullNameEn(UPDATED_FULL_NAME_EN)
            .grpNumber(UPDATED_GRP_NUMBER)
            .genderId(UPDATED_GENDER_ID)
            .birthDate(UPDATED_BIRTH_DATE);

        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiary))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testBeneficiary.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testBeneficiary.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testBeneficiary.getFullNameEn()).isEqualTo(UPDATED_FULL_NAME_EN);
        assertThat(testBeneficiary.getGrpNumber()).isEqualTo(UPDATED_GRP_NUMBER);
        assertThat(testBeneficiary.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testBeneficiary.getGenderId()).isEqualTo(UPDATED_GENDER_ID);
        assertThat(testBeneficiary.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBeneficiaryWithPatch() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary using partial update
        Beneficiary partialUpdatedBeneficiary = new Beneficiary();
        partialUpdatedBeneficiary.setId(beneficiary.getId());

        partialUpdatedBeneficiary
            .eid(UPDATED_EID)
            .uid(UPDATED_UID)
            .fullName(UPDATED_FULL_NAME)
            .fullNameEn(UPDATED_FULL_NAME_EN)
            .grpNumber(UPDATED_GRP_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .genderId(UPDATED_GENDER_ID)
            .birthDate(UPDATED_BIRTH_DATE);

        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiary))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testBeneficiary.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testBeneficiary.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testBeneficiary.getFullNameEn()).isEqualTo(UPDATED_FULL_NAME_EN);
        assertThat(testBeneficiary.getGrpNumber()).isEqualTo(UPDATED_GRP_NUMBER);
        assertThat(testBeneficiary.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testBeneficiary.getGenderId()).isEqualTo(UPDATED_GENDER_ID);
        assertThat(testBeneficiary.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beneficiary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeDelete = beneficiaryRepository.findAll().size();

        // Delete the beneficiary
        restBeneficiaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, beneficiary.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
