package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.HappEvntPlac;
import ae.gov.dubaipolice.happy.repository.HappEvntPlacRepository;
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
 * Integration tests for the {@link HappEvntPlacResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HappEvntPlacResourceIT {

    private static final String DEFAULT_PLACE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/happ-evnt-placs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HappEvntPlacRepository happEvntPlacRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHappEvntPlacMockMvc;

    private HappEvntPlac happEvntPlac;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappEvntPlac createEntity(EntityManager em) {
        HappEvntPlac happEvntPlac = new HappEvntPlac().placeName(DEFAULT_PLACE_NAME);
        return happEvntPlac;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappEvntPlac createUpdatedEntity(EntityManager em) {
        HappEvntPlac happEvntPlac = new HappEvntPlac().placeName(UPDATED_PLACE_NAME);
        return happEvntPlac;
    }

    @BeforeEach
    public void initTest() {
        happEvntPlac = createEntity(em);
    }

    @Test
    @Transactional
    void createHappEvntPlac() throws Exception {
        int databaseSizeBeforeCreate = happEvntPlacRepository.findAll().size();
        // Create the HappEvntPlac
        restHappEvntPlacMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isCreated());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeCreate + 1);
        HappEvntPlac testHappEvntPlac = happEvntPlacList.get(happEvntPlacList.size() - 1);
        assertThat(testHappEvntPlac.getPlaceName()).isEqualTo(DEFAULT_PLACE_NAME);
    }

    @Test
    @Transactional
    void createHappEvntPlacWithExistingId() throws Exception {
        // Create the HappEvntPlac with an existing ID
        happEvntPlac.setId(1L);

        int databaseSizeBeforeCreate = happEvntPlacRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHappEvntPlacMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlaceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = happEvntPlacRepository.findAll().size();
        // set the field null
        happEvntPlac.setPlaceName(null);

        // Create the HappEvntPlac, which fails.

        restHappEvntPlacMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHappEvntPlacs() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        // Get all the happEvntPlacList
        restHappEvntPlacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happEvntPlac.getId().intValue())))
            .andExpect(jsonPath("$.[*].placeName").value(hasItem(DEFAULT_PLACE_NAME)));
    }

    @Test
    @Transactional
    void getHappEvntPlac() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        // Get the happEvntPlac
        restHappEvntPlacMockMvc
            .perform(get(ENTITY_API_URL_ID, happEvntPlac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(happEvntPlac.getId().intValue()))
            .andExpect(jsonPath("$.placeName").value(DEFAULT_PLACE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHappEvntPlac() throws Exception {
        // Get the happEvntPlac
        restHappEvntPlacMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHappEvntPlac() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();

        // Update the happEvntPlac
        HappEvntPlac updatedHappEvntPlac = happEvntPlacRepository.findById(happEvntPlac.getId()).get();
        // Disconnect from session so that the updates on updatedHappEvntPlac are not directly saved in db
        em.detach(updatedHappEvntPlac);
        updatedHappEvntPlac.placeName(UPDATED_PLACE_NAME);

        restHappEvntPlacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHappEvntPlac.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHappEvntPlac))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
        HappEvntPlac testHappEvntPlac = happEvntPlacList.get(happEvntPlacList.size() - 1);
        assertThat(testHappEvntPlac.getPlaceName()).isEqualTo(UPDATED_PLACE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, happEvntPlac.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHappEvntPlacWithPatch() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();

        // Update the happEvntPlac using partial update
        HappEvntPlac partialUpdatedHappEvntPlac = new HappEvntPlac();
        partialUpdatedHappEvntPlac.setId(happEvntPlac.getId());

        partialUpdatedHappEvntPlac.placeName(UPDATED_PLACE_NAME);

        restHappEvntPlacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappEvntPlac.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappEvntPlac))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
        HappEvntPlac testHappEvntPlac = happEvntPlacList.get(happEvntPlacList.size() - 1);
        assertThat(testHappEvntPlac.getPlaceName()).isEqualTo(UPDATED_PLACE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHappEvntPlacWithPatch() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();

        // Update the happEvntPlac using partial update
        HappEvntPlac partialUpdatedHappEvntPlac = new HappEvntPlac();
        partialUpdatedHappEvntPlac.setId(happEvntPlac.getId());

        partialUpdatedHappEvntPlac.placeName(UPDATED_PLACE_NAME);

        restHappEvntPlacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappEvntPlac.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappEvntPlac))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
        HappEvntPlac testHappEvntPlac = happEvntPlacList.get(happEvntPlacList.size() - 1);
        assertThat(testHappEvntPlac.getPlaceName()).isEqualTo(UPDATED_PLACE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, happEvntPlac.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHappEvntPlac() throws Exception {
        int databaseSizeBeforeUpdate = happEvntPlacRepository.findAll().size();
        happEvntPlac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntPlacMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntPlac))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappEvntPlac in the database
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHappEvntPlac() throws Exception {
        // Initialize the database
        happEvntPlacRepository.saveAndFlush(happEvntPlac);

        int databaseSizeBeforeDelete = happEvntPlacRepository.findAll().size();

        // Delete the happEvntPlac
        restHappEvntPlacMockMvc
            .perform(delete(ENTITY_API_URL_ID, happEvntPlac.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HappEvntPlac> happEvntPlacList = happEvntPlacRepository.findAll();
        assertThat(happEvntPlacList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
