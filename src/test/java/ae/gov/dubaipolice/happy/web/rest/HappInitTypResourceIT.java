package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.HappInitTyp;
import ae.gov.dubaipolice.happy.repository.HappInitTypRepository;
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
 * Integration tests for the {@link HappInitTypResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HappInitTypResourceIT {

    private static final String DEFAULT_INIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INIT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/happ-init-typs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HappInitTypRepository happInitTypRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHappInitTypMockMvc;

    private HappInitTyp happInitTyp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappInitTyp createEntity(EntityManager em) {
        HappInitTyp happInitTyp = new HappInitTyp().initName(DEFAULT_INIT_NAME);
        return happInitTyp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappInitTyp createUpdatedEntity(EntityManager em) {
        HappInitTyp happInitTyp = new HappInitTyp().initName(UPDATED_INIT_NAME);
        return happInitTyp;
    }

    @BeforeEach
    public void initTest() {
        happInitTyp = createEntity(em);
    }

    @Test
    @Transactional
    void createHappInitTyp() throws Exception {
        int databaseSizeBeforeCreate = happInitTypRepository.findAll().size();
        // Create the HappInitTyp
        restHappInitTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isCreated());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeCreate + 1);
        HappInitTyp testHappInitTyp = happInitTypList.get(happInitTypList.size() - 1);
        assertThat(testHappInitTyp.getInitName()).isEqualTo(DEFAULT_INIT_NAME);
    }

    @Test
    @Transactional
    void createHappInitTypWithExistingId() throws Exception {
        // Create the HappInitTyp with an existing ID
        happInitTyp.setId(1L);

        int databaseSizeBeforeCreate = happInitTypRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHappInitTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInitNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = happInitTypRepository.findAll().size();
        // set the field null
        happInitTyp.setInitName(null);

        // Create the HappInitTyp, which fails.

        restHappInitTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHappInitTyps() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        // Get all the happInitTypList
        restHappInitTypMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happInitTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].initName").value(hasItem(DEFAULT_INIT_NAME)));
    }

    @Test
    @Transactional
    void getHappInitTyp() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        // Get the happInitTyp
        restHappInitTypMockMvc
            .perform(get(ENTITY_API_URL_ID, happInitTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(happInitTyp.getId().intValue()))
            .andExpect(jsonPath("$.initName").value(DEFAULT_INIT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHappInitTyp() throws Exception {
        // Get the happInitTyp
        restHappInitTypMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHappInitTyp() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();

        // Update the happInitTyp
        HappInitTyp updatedHappInitTyp = happInitTypRepository.findById(happInitTyp.getId()).get();
        // Disconnect from session so that the updates on updatedHappInitTyp are not directly saved in db
        em.detach(updatedHappInitTyp);
        updatedHappInitTyp.initName(UPDATED_INIT_NAME);

        restHappInitTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHappInitTyp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHappInitTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
        HappInitTyp testHappInitTyp = happInitTypList.get(happInitTypList.size() - 1);
        assertThat(testHappInitTyp.getInitName()).isEqualTo(UPDATED_INIT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, happInitTyp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHappInitTypWithPatch() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();

        // Update the happInitTyp using partial update
        HappInitTyp partialUpdatedHappInitTyp = new HappInitTyp();
        partialUpdatedHappInitTyp.setId(happInitTyp.getId());

        partialUpdatedHappInitTyp.initName(UPDATED_INIT_NAME);

        restHappInitTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappInitTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappInitTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
        HappInitTyp testHappInitTyp = happInitTypList.get(happInitTypList.size() - 1);
        assertThat(testHappInitTyp.getInitName()).isEqualTo(UPDATED_INIT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHappInitTypWithPatch() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();

        // Update the happInitTyp using partial update
        HappInitTyp partialUpdatedHappInitTyp = new HappInitTyp();
        partialUpdatedHappInitTyp.setId(happInitTyp.getId());

        partialUpdatedHappInitTyp.initName(UPDATED_INIT_NAME);

        restHappInitTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappInitTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappInitTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
        HappInitTyp testHappInitTyp = happInitTypList.get(happInitTypList.size() - 1);
        assertThat(testHappInitTyp.getInitName()).isEqualTo(UPDATED_INIT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, happInitTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHappInitTyp() throws Exception {
        int databaseSizeBeforeUpdate = happInitTypRepository.findAll().size();
        happInitTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitTypMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitTyp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappInitTyp in the database
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHappInitTyp() throws Exception {
        // Initialize the database
        happInitTypRepository.saveAndFlush(happInitTyp);

        int databaseSizeBeforeDelete = happInitTypRepository.findAll().size();

        // Delete the happInitTyp
        restHappInitTypMockMvc
            .perform(delete(ENTITY_API_URL_ID, happInitTyp.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HappInitTyp> happInitTypList = happInitTypRepository.findAll();
        assertThat(happInitTypList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
