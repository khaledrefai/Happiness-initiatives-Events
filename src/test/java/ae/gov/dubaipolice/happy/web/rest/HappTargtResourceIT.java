package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.HappTargt;
import ae.gov.dubaipolice.happy.repository.HappTargtRepository;
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
 * Integration tests for the {@link HappTargtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HappTargtResourceIT {

    private static final String DEFAULT_TARGET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/happ-targts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HappTargtRepository happTargtRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHappTargtMockMvc;

    private HappTargt happTargt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappTargt createEntity(EntityManager em) {
        HappTargt happTargt = new HappTargt().targetName(DEFAULT_TARGET_NAME);
        return happTargt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappTargt createUpdatedEntity(EntityManager em) {
        HappTargt happTargt = new HappTargt().targetName(UPDATED_TARGET_NAME);
        return happTargt;
    }

    @BeforeEach
    public void initTest() {
        happTargt = createEntity(em);
    }

    @Test
    @Transactional
    void createHappTargt() throws Exception {
        int databaseSizeBeforeCreate = happTargtRepository.findAll().size();
        // Create the HappTargt
        restHappTargtMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isCreated());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeCreate + 1);
        HappTargt testHappTargt = happTargtList.get(happTargtList.size() - 1);
        assertThat(testHappTargt.getTargetName()).isEqualTo(DEFAULT_TARGET_NAME);
    }

    @Test
    @Transactional
    void createHappTargtWithExistingId() throws Exception {
        // Create the HappTargt with an existing ID
        happTargt.setId(1L);

        int databaseSizeBeforeCreate = happTargtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHappTargtMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTargetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = happTargtRepository.findAll().size();
        // set the field null
        happTargt.setTargetName(null);

        // Create the HappTargt, which fails.

        restHappTargtMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHappTargts() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        // Get all the happTargtList
        restHappTargtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happTargt.getId().intValue())))
            .andExpect(jsonPath("$.[*].targetName").value(hasItem(DEFAULT_TARGET_NAME)));
    }

    @Test
    @Transactional
    void getHappTargt() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        // Get the happTargt
        restHappTargtMockMvc
            .perform(get(ENTITY_API_URL_ID, happTargt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(happTargt.getId().intValue()))
            .andExpect(jsonPath("$.targetName").value(DEFAULT_TARGET_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHappTargt() throws Exception {
        // Get the happTargt
        restHappTargtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHappTargt() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();

        // Update the happTargt
        HappTargt updatedHappTargt = happTargtRepository.findById(happTargt.getId()).get();
        // Disconnect from session so that the updates on updatedHappTargt are not directly saved in db
        em.detach(updatedHappTargt);
        updatedHappTargt.targetName(UPDATED_TARGET_NAME);

        restHappTargtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHappTargt.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHappTargt))
            )
            .andExpect(status().isOk());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
        HappTargt testHappTargt = happTargtList.get(happTargtList.size() - 1);
        assertThat(testHappTargt.getTargetName()).isEqualTo(UPDATED_TARGET_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, happTargt.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHappTargtWithPatch() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();

        // Update the happTargt using partial update
        HappTargt partialUpdatedHappTargt = new HappTargt();
        partialUpdatedHappTargt.setId(happTargt.getId());

        partialUpdatedHappTargt.targetName(UPDATED_TARGET_NAME);

        restHappTargtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappTargt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappTargt))
            )
            .andExpect(status().isOk());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
        HappTargt testHappTargt = happTargtList.get(happTargtList.size() - 1);
        assertThat(testHappTargt.getTargetName()).isEqualTo(UPDATED_TARGET_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHappTargtWithPatch() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();

        // Update the happTargt using partial update
        HappTargt partialUpdatedHappTargt = new HappTargt();
        partialUpdatedHappTargt.setId(happTargt.getId());

        partialUpdatedHappTargt.targetName(UPDATED_TARGET_NAME);

        restHappTargtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappTargt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappTargt))
            )
            .andExpect(status().isOk());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
        HappTargt testHappTargt = happTargtList.get(happTargtList.size() - 1);
        assertThat(testHappTargt.getTargetName()).isEqualTo(UPDATED_TARGET_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, happTargt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHappTargt() throws Exception {
        int databaseSizeBeforeUpdate = happTargtRepository.findAll().size();
        happTargt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappTargtMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happTargt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappTargt in the database
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHappTargt() throws Exception {
        // Initialize the database
        happTargtRepository.saveAndFlush(happTargt);

        int databaseSizeBeforeDelete = happTargtRepository.findAll().size();

        // Delete the happTargt
        restHappTargtMockMvc
            .perform(delete(ENTITY_API_URL_ID, happTargt.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HappTargt> happTargtList = happTargtRepository.findAll();
        assertThat(happTargtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
