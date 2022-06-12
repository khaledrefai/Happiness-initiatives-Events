package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.HappEvntTyp;
import ae.gov.dubaipolice.happy.repository.HappEvntTypRepository;
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
 * Integration tests for the {@link HappEvntTypResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HappEvntTypResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/happ-evnt-typs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HappEvntTypRepository happEvntTypRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHappEvntTypMockMvc;

    private HappEvntTyp happEvntTyp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappEvntTyp createEntity(EntityManager em) {
        HappEvntTyp happEvntTyp = new HappEvntTyp().eventName(DEFAULT_EVENT_NAME);
        return happEvntTyp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappEvntTyp createUpdatedEntity(EntityManager em) {
        HappEvntTyp happEvntTyp = new HappEvntTyp().eventName(UPDATED_EVENT_NAME);
        return happEvntTyp;
    }

    @BeforeEach
    public void initTest() {
        happEvntTyp = createEntity(em);
    }

    @Test
    @Transactional
    void createHappEvntTyp() throws Exception {
        int databaseSizeBeforeCreate = happEvntTypRepository.findAll().size();
        // Create the HappEvntTyp
        restHappEvntTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isCreated());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeCreate + 1);
        HappEvntTyp testHappEvntTyp = happEvntTypList.get(happEvntTypList.size() - 1);
        assertThat(testHappEvntTyp.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
    }

    @Test
    @Transactional
    void createHappEvntTypWithExistingId() throws Exception {
        // Create the HappEvntTyp with an existing ID
        happEvntTyp.setId(1L);

        int databaseSizeBeforeCreate = happEvntTypRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHappEvntTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = happEvntTypRepository.findAll().size();
        // set the field null
        happEvntTyp.setEventName(null);

        // Create the HappEvntTyp, which fails.

        restHappEvntTypMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHappEvntTyps() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        // Get all the happEvntTypList
        restHappEvntTypMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happEvntTyp.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)));
    }

    @Test
    @Transactional
    void getHappEvntTyp() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        // Get the happEvntTyp
        restHappEvntTypMockMvc
            .perform(get(ENTITY_API_URL_ID, happEvntTyp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(happEvntTyp.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHappEvntTyp() throws Exception {
        // Get the happEvntTyp
        restHappEvntTypMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHappEvntTyp() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();

        // Update the happEvntTyp
        HappEvntTyp updatedHappEvntTyp = happEvntTypRepository.findById(happEvntTyp.getId()).get();
        // Disconnect from session so that the updates on updatedHappEvntTyp are not directly saved in db
        em.detach(updatedHappEvntTyp);
        updatedHappEvntTyp.eventName(UPDATED_EVENT_NAME);

        restHappEvntTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHappEvntTyp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHappEvntTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
        HappEvntTyp testHappEvntTyp = happEvntTypList.get(happEvntTypList.size() - 1);
        assertThat(testHappEvntTyp.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, happEvntTyp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHappEvntTypWithPatch() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();

        // Update the happEvntTyp using partial update
        HappEvntTyp partialUpdatedHappEvntTyp = new HappEvntTyp();
        partialUpdatedHappEvntTyp.setId(happEvntTyp.getId());

        restHappEvntTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappEvntTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappEvntTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
        HappEvntTyp testHappEvntTyp = happEvntTypList.get(happEvntTypList.size() - 1);
        assertThat(testHappEvntTyp.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHappEvntTypWithPatch() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();

        // Update the happEvntTyp using partial update
        HappEvntTyp partialUpdatedHappEvntTyp = new HappEvntTyp();
        partialUpdatedHappEvntTyp.setId(happEvntTyp.getId());

        partialUpdatedHappEvntTyp.eventName(UPDATED_EVENT_NAME);

        restHappEvntTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappEvntTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappEvntTyp))
            )
            .andExpect(status().isOk());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
        HappEvntTyp testHappEvntTyp = happEvntTypList.get(happEvntTypList.size() - 1);
        assertThat(testHappEvntTyp.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, happEvntTyp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHappEvntTyp() throws Exception {
        int databaseSizeBeforeUpdate = happEvntTypRepository.findAll().size();
        happEvntTyp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappEvntTypMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happEvntTyp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappEvntTyp in the database
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHappEvntTyp() throws Exception {
        // Initialize the database
        happEvntTypRepository.saveAndFlush(happEvntTyp);

        int databaseSizeBeforeDelete = happEvntTypRepository.findAll().size();

        // Delete the happEvntTyp
        restHappEvntTypMockMvc
            .perform(delete(ENTITY_API_URL_ID, happEvntTyp.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HappEvntTyp> happEvntTypList = happEvntTypRepository.findAll();
        assertThat(happEvntTypList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
