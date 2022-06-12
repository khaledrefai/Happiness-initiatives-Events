package ae.gov.dubaipolice.happy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ae.gov.dubaipolice.happy.IntegrationTest;
import ae.gov.dubaipolice.happy.domain.Attachment;
import ae.gov.dubaipolice.happy.domain.Beneficiary;
import ae.gov.dubaipolice.happy.domain.Employee;
import ae.gov.dubaipolice.happy.domain.HappEvntPlac;
import ae.gov.dubaipolice.happy.domain.HappEvntTyp;
import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import ae.gov.dubaipolice.happy.domain.HappInitTyp;
import ae.gov.dubaipolice.happy.domain.HappTargt;
import ae.gov.dubaipolice.happy.domain.enumeration.InitOrEvent;
import ae.gov.dubaipolice.happy.repository.HappInitEvntRepository;
import ae.gov.dubaipolice.happy.service.HappInitEvntService;
import ae.gov.dubaipolice.happy.service.criteria.HappInitEvntCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HappInitEvntResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HappInitEvntResourceIT {

    private static final String DEFAULT_INIT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INIT_EVENT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_TO = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL_TARGET = 1;
    private static final Integer UPDATED_TOTAL_TARGET = 2;
    private static final Integer SMALLER_TOTAL_TARGET = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final InitOrEvent DEFAULT_INIT_OR_EVENT = InitOrEvent.INITIATIVE;
    private static final InitOrEvent UPDATED_INIT_OR_EVENT = InitOrEvent.EVENT;

    private static final LocalDate DEFAULT_ADD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADD_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ADD_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_ADD_BY = 1L;
    private static final Long UPDATED_ADD_BY = 2L;
    private static final Long SMALLER_ADD_BY = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/happ-init-evnts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HappInitEvntRepository happInitEvntRepository;

    @Mock
    private HappInitEvntRepository happInitEvntRepositoryMock;

    @Mock
    private HappInitEvntService happInitEvntServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHappInitEvntMockMvc;

    private HappInitEvnt happInitEvnt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappInitEvnt createEntity(EntityManager em) {
        HappInitEvnt happInitEvnt = new HappInitEvnt()
            .initEventName(DEFAULT_INIT_EVENT_NAME)
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .totalTarget(DEFAULT_TOTAL_TARGET)
            .notes(DEFAULT_NOTES)
            .initOrEvent(DEFAULT_INIT_OR_EVENT)
            .addDate(DEFAULT_ADD_DATE)
            .addBy(DEFAULT_ADD_BY);
        return happInitEvnt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HappInitEvnt createUpdatedEntity(EntityManager em) {
        HappInitEvnt happInitEvnt = new HappInitEvnt()
            .initEventName(UPDATED_INIT_EVENT_NAME)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .totalTarget(UPDATED_TOTAL_TARGET)
            .notes(UPDATED_NOTES)
            .initOrEvent(UPDATED_INIT_OR_EVENT)
            .addDate(UPDATED_ADD_DATE)
            .addBy(UPDATED_ADD_BY);
        return happInitEvnt;
    }

    @BeforeEach
    public void initTest() {
        happInitEvnt = createEntity(em);
    }

    @Test
    @Transactional
    void createHappInitEvnt() throws Exception {
        int databaseSizeBeforeCreate = happInitEvntRepository.findAll().size();
        // Create the HappInitEvnt
        restHappInitEvntMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isCreated());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeCreate + 1);
        HappInitEvnt testHappInitEvnt = happInitEvntList.get(happInitEvntList.size() - 1);
        assertThat(testHappInitEvnt.getInitEventName()).isEqualTo(DEFAULT_INIT_EVENT_NAME);
        assertThat(testHappInitEvnt.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testHappInitEvnt.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testHappInitEvnt.getTotalTarget()).isEqualTo(DEFAULT_TOTAL_TARGET);
        assertThat(testHappInitEvnt.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testHappInitEvnt.getInitOrEvent()).isEqualTo(DEFAULT_INIT_OR_EVENT);
        assertThat(testHappInitEvnt.getAddDate()).isEqualTo(DEFAULT_ADD_DATE);
        assertThat(testHappInitEvnt.getAddBy()).isEqualTo(DEFAULT_ADD_BY);
    }

    @Test
    @Transactional
    void createHappInitEvntWithExistingId() throws Exception {
        // Create the HappInitEvnt with an existing ID
        happInitEvnt.setId(1L);

        int databaseSizeBeforeCreate = happInitEvntRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHappInitEvntMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHappInitEvnts() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happInitEvnt.getId().intValue())))
            .andExpect(jsonPath("$.[*].initEventName").value(hasItem(DEFAULT_INIT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].totalTarget").value(hasItem(DEFAULT_TOTAL_TARGET)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].initOrEvent").value(hasItem(DEFAULT_INIT_OR_EVENT.toString())))
            .andExpect(jsonPath("$.[*].addDate").value(hasItem(DEFAULT_ADD_DATE.toString())))
            .andExpect(jsonPath("$.[*].addBy").value(hasItem(DEFAULT_ADD_BY.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHappInitEvntsWithEagerRelationshipsIsEnabled() throws Exception {
        when(happInitEvntServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHappInitEvntMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(happInitEvntServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHappInitEvntsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(happInitEvntServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHappInitEvntMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(happInitEvntServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getHappInitEvnt() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get the happInitEvnt
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL_ID, happInitEvnt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(happInitEvnt.getId().intValue()))
            .andExpect(jsonPath("$.initEventName").value(DEFAULT_INIT_EVENT_NAME))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
            .andExpect(jsonPath("$.totalTarget").value(DEFAULT_TOTAL_TARGET))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.initOrEvent").value(DEFAULT_INIT_OR_EVENT.toString()))
            .andExpect(jsonPath("$.addDate").value(DEFAULT_ADD_DATE.toString()))
            .andExpect(jsonPath("$.addBy").value(DEFAULT_ADD_BY.intValue()));
    }

    @Test
    @Transactional
    void getHappInitEvntsByIdFiltering() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        Long id = happInitEvnt.getId();

        defaultHappInitEvntShouldBeFound("id.equals=" + id);
        defaultHappInitEvntShouldNotBeFound("id.notEquals=" + id);

        defaultHappInitEvntShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHappInitEvntShouldNotBeFound("id.greaterThan=" + id);

        defaultHappInitEvntShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHappInitEvntShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName equals to DEFAULT_INIT_EVENT_NAME
        defaultHappInitEvntShouldBeFound("initEventName.equals=" + DEFAULT_INIT_EVENT_NAME);

        // Get all the happInitEvntList where initEventName equals to UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldNotBeFound("initEventName.equals=" + UPDATED_INIT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName not equals to DEFAULT_INIT_EVENT_NAME
        defaultHappInitEvntShouldNotBeFound("initEventName.notEquals=" + DEFAULT_INIT_EVENT_NAME);

        // Get all the happInitEvntList where initEventName not equals to UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldBeFound("initEventName.notEquals=" + UPDATED_INIT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName in DEFAULT_INIT_EVENT_NAME or UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldBeFound("initEventName.in=" + DEFAULT_INIT_EVENT_NAME + "," + UPDATED_INIT_EVENT_NAME);

        // Get all the happInitEvntList where initEventName equals to UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldNotBeFound("initEventName.in=" + UPDATED_INIT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName is not null
        defaultHappInitEvntShouldBeFound("initEventName.specified=true");

        // Get all the happInitEvntList where initEventName is null
        defaultHappInitEvntShouldNotBeFound("initEventName.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameContainsSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName contains DEFAULT_INIT_EVENT_NAME
        defaultHappInitEvntShouldBeFound("initEventName.contains=" + DEFAULT_INIT_EVENT_NAME);

        // Get all the happInitEvntList where initEventName contains UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldNotBeFound("initEventName.contains=" + UPDATED_INIT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitEventNameNotContainsSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initEventName does not contain DEFAULT_INIT_EVENT_NAME
        defaultHappInitEvntShouldNotBeFound("initEventName.doesNotContain=" + DEFAULT_INIT_EVENT_NAME);

        // Get all the happInitEvntList where initEventName does not contain UPDATED_INIT_EVENT_NAME
        defaultHappInitEvntShouldBeFound("initEventName.doesNotContain=" + UPDATED_INIT_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom equals to DEFAULT_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.equals=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom equals to UPDATED_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.equals=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom not equals to DEFAULT_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.notEquals=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom not equals to UPDATED_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.notEquals=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom in DEFAULT_DATE_FROM or UPDATED_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.in=" + DEFAULT_DATE_FROM + "," + UPDATED_DATE_FROM);

        // Get all the happInitEvntList where dateFrom equals to UPDATED_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.in=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom is not null
        defaultHappInitEvntShouldBeFound("dateFrom.specified=true");

        // Get all the happInitEvntList where dateFrom is null
        defaultHappInitEvntShouldNotBeFound("dateFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom is greater than or equal to DEFAULT_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.greaterThanOrEqual=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom is greater than or equal to UPDATED_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.greaterThanOrEqual=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom is less than or equal to DEFAULT_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.lessThanOrEqual=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom is less than or equal to SMALLER_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.lessThanOrEqual=" + SMALLER_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsLessThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom is less than DEFAULT_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.lessThan=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom is less than UPDATED_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.lessThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateFrom is greater than DEFAULT_DATE_FROM
        defaultHappInitEvntShouldNotBeFound("dateFrom.greaterThan=" + DEFAULT_DATE_FROM);

        // Get all the happInitEvntList where dateFrom is greater than SMALLER_DATE_FROM
        defaultHappInitEvntShouldBeFound("dateFrom.greaterThan=" + SMALLER_DATE_FROM);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo equals to DEFAULT_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.equals=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo equals to UPDATED_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.equals=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo not equals to DEFAULT_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.notEquals=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo not equals to UPDATED_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.notEquals=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo in DEFAULT_DATE_TO or UPDATED_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.in=" + DEFAULT_DATE_TO + "," + UPDATED_DATE_TO);

        // Get all the happInitEvntList where dateTo equals to UPDATED_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.in=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo is not null
        defaultHappInitEvntShouldBeFound("dateTo.specified=true");

        // Get all the happInitEvntList where dateTo is null
        defaultHappInitEvntShouldNotBeFound("dateTo.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo is greater than or equal to DEFAULT_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.greaterThanOrEqual=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo is greater than or equal to UPDATED_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.greaterThanOrEqual=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo is less than or equal to DEFAULT_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.lessThanOrEqual=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo is less than or equal to SMALLER_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.lessThanOrEqual=" + SMALLER_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsLessThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo is less than DEFAULT_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.lessThan=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo is less than UPDATED_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.lessThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByDateToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where dateTo is greater than DEFAULT_DATE_TO
        defaultHappInitEvntShouldNotBeFound("dateTo.greaterThan=" + DEFAULT_DATE_TO);

        // Get all the happInitEvntList where dateTo is greater than SMALLER_DATE_TO
        defaultHappInitEvntShouldBeFound("dateTo.greaterThan=" + SMALLER_DATE_TO);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget equals to DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.equals=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget equals to UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.equals=" + UPDATED_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget not equals to DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.notEquals=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget not equals to UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.notEquals=" + UPDATED_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget in DEFAULT_TOTAL_TARGET or UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.in=" + DEFAULT_TOTAL_TARGET + "," + UPDATED_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget equals to UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.in=" + UPDATED_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget is not null
        defaultHappInitEvntShouldBeFound("totalTarget.specified=true");

        // Get all the happInitEvntList where totalTarget is null
        defaultHappInitEvntShouldNotBeFound("totalTarget.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget is greater than or equal to DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.greaterThanOrEqual=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget is greater than or equal to UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.greaterThanOrEqual=" + UPDATED_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget is less than or equal to DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.lessThanOrEqual=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget is less than or equal to SMALLER_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.lessThanOrEqual=" + SMALLER_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsLessThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget is less than DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.lessThan=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget is less than UPDATED_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.lessThan=" + UPDATED_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByTotalTargetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where totalTarget is greater than DEFAULT_TOTAL_TARGET
        defaultHappInitEvntShouldNotBeFound("totalTarget.greaterThan=" + DEFAULT_TOTAL_TARGET);

        // Get all the happInitEvntList where totalTarget is greater than SMALLER_TOTAL_TARGET
        defaultHappInitEvntShouldBeFound("totalTarget.greaterThan=" + SMALLER_TOTAL_TARGET);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes equals to DEFAULT_NOTES
        defaultHappInitEvntShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the happInitEvntList where notes equals to UPDATED_NOTES
        defaultHappInitEvntShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes not equals to DEFAULT_NOTES
        defaultHappInitEvntShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the happInitEvntList where notes not equals to UPDATED_NOTES
        defaultHappInitEvntShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultHappInitEvntShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the happInitEvntList where notes equals to UPDATED_NOTES
        defaultHappInitEvntShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes is not null
        defaultHappInitEvntShouldBeFound("notes.specified=true");

        // Get all the happInitEvntList where notes is null
        defaultHappInitEvntShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesContainsSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes contains DEFAULT_NOTES
        defaultHappInitEvntShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the happInitEvntList where notes contains UPDATED_NOTES
        defaultHappInitEvntShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where notes does not contain DEFAULT_NOTES
        defaultHappInitEvntShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the happInitEvntList where notes does not contain UPDATED_NOTES
        defaultHappInitEvntShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitOrEventIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initOrEvent equals to DEFAULT_INIT_OR_EVENT
        defaultHappInitEvntShouldBeFound("initOrEvent.equals=" + DEFAULT_INIT_OR_EVENT);

        // Get all the happInitEvntList where initOrEvent equals to UPDATED_INIT_OR_EVENT
        defaultHappInitEvntShouldNotBeFound("initOrEvent.equals=" + UPDATED_INIT_OR_EVENT);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitOrEventIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initOrEvent not equals to DEFAULT_INIT_OR_EVENT
        defaultHappInitEvntShouldNotBeFound("initOrEvent.notEquals=" + DEFAULT_INIT_OR_EVENT);

        // Get all the happInitEvntList where initOrEvent not equals to UPDATED_INIT_OR_EVENT
        defaultHappInitEvntShouldBeFound("initOrEvent.notEquals=" + UPDATED_INIT_OR_EVENT);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitOrEventIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initOrEvent in DEFAULT_INIT_OR_EVENT or UPDATED_INIT_OR_EVENT
        defaultHappInitEvntShouldBeFound("initOrEvent.in=" + DEFAULT_INIT_OR_EVENT + "," + UPDATED_INIT_OR_EVENT);

        // Get all the happInitEvntList where initOrEvent equals to UPDATED_INIT_OR_EVENT
        defaultHappInitEvntShouldNotBeFound("initOrEvent.in=" + UPDATED_INIT_OR_EVENT);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByInitOrEventIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where initOrEvent is not null
        defaultHappInitEvntShouldBeFound("initOrEvent.specified=true");

        // Get all the happInitEvntList where initOrEvent is null
        defaultHappInitEvntShouldNotBeFound("initOrEvent.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate equals to DEFAULT_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.equals=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate equals to UPDATED_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.equals=" + UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate not equals to DEFAULT_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.notEquals=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate not equals to UPDATED_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.notEquals=" + UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate in DEFAULT_ADD_DATE or UPDATED_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.in=" + DEFAULT_ADD_DATE + "," + UPDATED_ADD_DATE);

        // Get all the happInitEvntList where addDate equals to UPDATED_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.in=" + UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate is not null
        defaultHappInitEvntShouldBeFound("addDate.specified=true");

        // Get all the happInitEvntList where addDate is null
        defaultHappInitEvntShouldNotBeFound("addDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate is greater than or equal to DEFAULT_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.greaterThanOrEqual=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate is greater than or equal to UPDATED_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.greaterThanOrEqual=" + UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate is less than or equal to DEFAULT_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.lessThanOrEqual=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate is less than or equal to SMALLER_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.lessThanOrEqual=" + SMALLER_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsLessThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate is less than DEFAULT_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.lessThan=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate is less than UPDATED_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.lessThan=" + UPDATED_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addDate is greater than DEFAULT_ADD_DATE
        defaultHappInitEvntShouldNotBeFound("addDate.greaterThan=" + DEFAULT_ADD_DATE);

        // Get all the happInitEvntList where addDate is greater than SMALLER_ADD_DATE
        defaultHappInitEvntShouldBeFound("addDate.greaterThan=" + SMALLER_ADD_DATE);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy equals to DEFAULT_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.equals=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy equals to UPDATED_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.equals=" + UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy not equals to DEFAULT_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.notEquals=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy not equals to UPDATED_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.notEquals=" + UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsInShouldWork() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy in DEFAULT_ADD_BY or UPDATED_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.in=" + DEFAULT_ADD_BY + "," + UPDATED_ADD_BY);

        // Get all the happInitEvntList where addBy equals to UPDATED_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.in=" + UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsNullOrNotNull() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy is not null
        defaultHappInitEvntShouldBeFound("addBy.specified=true");

        // Get all the happInitEvntList where addBy is null
        defaultHappInitEvntShouldNotBeFound("addBy.specified=false");
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy is greater than or equal to DEFAULT_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.greaterThanOrEqual=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy is greater than or equal to UPDATED_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.greaterThanOrEqual=" + UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy is less than or equal to DEFAULT_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.lessThanOrEqual=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy is less than or equal to SMALLER_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.lessThanOrEqual=" + SMALLER_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsLessThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy is less than DEFAULT_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.lessThan=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy is less than UPDATED_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.lessThan=" + UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAddByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        // Get all the happInitEvntList where addBy is greater than DEFAULT_ADD_BY
        defaultHappInitEvntShouldNotBeFound("addBy.greaterThan=" + DEFAULT_ADD_BY);

        // Get all the happInitEvntList where addBy is greater than SMALLER_ADD_BY
        defaultHappInitEvntShouldBeFound("addBy.greaterThan=" + SMALLER_ADD_BY);
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity(em);
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        em.persist(attachment);
        em.flush();
        happInitEvnt.addAttachment(attachment);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long attachmentId = attachment.getId();

        // Get all the happInitEvntList where attachment equals to attachmentId
        defaultHappInitEvntShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the happInitEvntList where attachment equals to (attachmentId + 1)
        defaultHappInitEvntShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByHappEvntTypIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        HappEvntTyp happEvntTyp;
        if (TestUtil.findAll(em, HappEvntTyp.class).isEmpty()) {
            happEvntTyp = HappEvntTypResourceIT.createEntity(em);
            em.persist(happEvntTyp);
            em.flush();
        } else {
            happEvntTyp = TestUtil.findAll(em, HappEvntTyp.class).get(0);
        }
        em.persist(happEvntTyp);
        em.flush();
        happInitEvnt.setHappEvntTyp(happEvntTyp);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long happEvntTypId = happEvntTyp.getId();

        // Get all the happInitEvntList where happEvntTyp equals to happEvntTypId
        defaultHappInitEvntShouldBeFound("happEvntTypId.equals=" + happEvntTypId);

        // Get all the happInitEvntList where happEvntTyp equals to (happEvntTypId + 1)
        defaultHappInitEvntShouldNotBeFound("happEvntTypId.equals=" + (happEvntTypId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByHappInitTypIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        HappInitTyp happInitTyp;
        if (TestUtil.findAll(em, HappInitTyp.class).isEmpty()) {
            happInitTyp = HappInitTypResourceIT.createEntity(em);
            em.persist(happInitTyp);
            em.flush();
        } else {
            happInitTyp = TestUtil.findAll(em, HappInitTyp.class).get(0);
        }
        em.persist(happInitTyp);
        em.flush();
        happInitEvnt.setHappInitTyp(happInitTyp);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long happInitTypId = happInitTyp.getId();

        // Get all the happInitEvntList where happInitTyp equals to happInitTypId
        defaultHappInitEvntShouldBeFound("happInitTypId.equals=" + happInitTypId);

        // Get all the happInitEvntList where happInitTyp equals to (happInitTypId + 1)
        defaultHappInitEvntShouldNotBeFound("happInitTypId.equals=" + (happInitTypId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByHappTargtIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        HappTargt happTargt;
        if (TestUtil.findAll(em, HappTargt.class).isEmpty()) {
            happTargt = HappTargtResourceIT.createEntity(em);
            em.persist(happTargt);
            em.flush();
        } else {
            happTargt = TestUtil.findAll(em, HappTargt.class).get(0);
        }
        em.persist(happTargt);
        em.flush();
        happInitEvnt.setHappTargt(happTargt);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long happTargtId = happTargt.getId();

        // Get all the happInitEvntList where happTargt equals to happTargtId
        defaultHappInitEvntShouldBeFound("happTargtId.equals=" + happTargtId);

        // Get all the happInitEvntList where happTargt equals to (happTargtId + 1)
        defaultHappInitEvntShouldNotBeFound("happTargtId.equals=" + (happTargtId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByHappEvntPlacIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        HappEvntPlac happEvntPlac;
        if (TestUtil.findAll(em, HappEvntPlac.class).isEmpty()) {
            happEvntPlac = HappEvntPlacResourceIT.createEntity(em);
            em.persist(happEvntPlac);
            em.flush();
        } else {
            happEvntPlac = TestUtil.findAll(em, HappEvntPlac.class).get(0);
        }
        em.persist(happEvntPlac);
        em.flush();
        happInitEvnt.setHappEvntPlac(happEvntPlac);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long happEvntPlacId = happEvntPlac.getId();

        // Get all the happInitEvntList where happEvntPlac equals to happEvntPlacId
        defaultHappInitEvntShouldBeFound("happEvntPlacId.equals=" + happEvntPlacId);

        // Get all the happInitEvntList where happEvntPlac equals to (happEvntPlacId + 1)
        defaultHappInitEvntShouldNotBeFound("happEvntPlacId.equals=" + (happEvntPlacId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        happInitEvnt.addEmployee(employee);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long employeeId = employee.getId();

        // Get all the happInitEvntList where employee equals to employeeId
        defaultHappInitEvntShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the happInitEvntList where employee equals to (employeeId + 1)
        defaultHappInitEvntShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllHappInitEvntsByBeneficiaryIsEqualToSomething() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Beneficiary beneficiary;
        if (TestUtil.findAll(em, Beneficiary.class).isEmpty()) {
            beneficiary = BeneficiaryResourceIT.createEntity(em);
            em.persist(beneficiary);
            em.flush();
        } else {
            beneficiary = TestUtil.findAll(em, Beneficiary.class).get(0);
        }
        em.persist(beneficiary);
        em.flush();
        happInitEvnt.addBeneficiary(beneficiary);
        happInitEvntRepository.saveAndFlush(happInitEvnt);
        Long beneficiaryId = beneficiary.getId();

        // Get all the happInitEvntList where beneficiary equals to beneficiaryId
        defaultHappInitEvntShouldBeFound("beneficiaryId.equals=" + beneficiaryId);

        // Get all the happInitEvntList where beneficiary equals to (beneficiaryId + 1)
        defaultHappInitEvntShouldNotBeFound("beneficiaryId.equals=" + (beneficiaryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHappInitEvntShouldBeFound(String filter) throws Exception {
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(happInitEvnt.getId().intValue())))
            .andExpect(jsonPath("$.[*].initEventName").value(hasItem(DEFAULT_INIT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].totalTarget").value(hasItem(DEFAULT_TOTAL_TARGET)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].initOrEvent").value(hasItem(DEFAULT_INIT_OR_EVENT.toString())))
            .andExpect(jsonPath("$.[*].addDate").value(hasItem(DEFAULT_ADD_DATE.toString())))
            .andExpect(jsonPath("$.[*].addBy").value(hasItem(DEFAULT_ADD_BY.intValue())));

        // Check, that the count call also returns 1
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHappInitEvntShouldNotBeFound(String filter) throws Exception {
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHappInitEvntMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHappInitEvnt() throws Exception {
        // Get the happInitEvnt
        restHappInitEvntMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHappInitEvnt() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();

        // Update the happInitEvnt
        HappInitEvnt updatedHappInitEvnt = happInitEvntRepository.findById(happInitEvnt.getId()).get();
        // Disconnect from session so that the updates on updatedHappInitEvnt are not directly saved in db
        em.detach(updatedHappInitEvnt);
        updatedHappInitEvnt
            .initEventName(UPDATED_INIT_EVENT_NAME)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .totalTarget(UPDATED_TOTAL_TARGET)
            .notes(UPDATED_NOTES)
            .initOrEvent(UPDATED_INIT_OR_EVENT)
            .addDate(UPDATED_ADD_DATE)
            .addBy(UPDATED_ADD_BY);

        restHappInitEvntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHappInitEvnt.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHappInitEvnt))
            )
            .andExpect(status().isOk());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
        HappInitEvnt testHappInitEvnt = happInitEvntList.get(happInitEvntList.size() - 1);
        assertThat(testHappInitEvnt.getInitEventName()).isEqualTo(UPDATED_INIT_EVENT_NAME);
        assertThat(testHappInitEvnt.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testHappInitEvnt.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testHappInitEvnt.getTotalTarget()).isEqualTo(UPDATED_TOTAL_TARGET);
        assertThat(testHappInitEvnt.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHappInitEvnt.getInitOrEvent()).isEqualTo(UPDATED_INIT_OR_EVENT);
        assertThat(testHappInitEvnt.getAddDate()).isEqualTo(UPDATED_ADD_DATE);
        assertThat(testHappInitEvnt.getAddBy()).isEqualTo(UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void putNonExistingHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, happInitEvnt.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHappInitEvntWithPatch() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();

        // Update the happInitEvnt using partial update
        HappInitEvnt partialUpdatedHappInitEvnt = new HappInitEvnt();
        partialUpdatedHappInitEvnt.setId(happInitEvnt.getId());

        partialUpdatedHappInitEvnt
            .initEventName(UPDATED_INIT_EVENT_NAME)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .addDate(UPDATED_ADD_DATE)
            .addBy(UPDATED_ADD_BY);

        restHappInitEvntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappInitEvnt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappInitEvnt))
            )
            .andExpect(status().isOk());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
        HappInitEvnt testHappInitEvnt = happInitEvntList.get(happInitEvntList.size() - 1);
        assertThat(testHappInitEvnt.getInitEventName()).isEqualTo(UPDATED_INIT_EVENT_NAME);
        assertThat(testHappInitEvnt.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testHappInitEvnt.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testHappInitEvnt.getTotalTarget()).isEqualTo(DEFAULT_TOTAL_TARGET);
        assertThat(testHappInitEvnt.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testHappInitEvnt.getInitOrEvent()).isEqualTo(DEFAULT_INIT_OR_EVENT);
        assertThat(testHappInitEvnt.getAddDate()).isEqualTo(UPDATED_ADD_DATE);
        assertThat(testHappInitEvnt.getAddBy()).isEqualTo(UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void fullUpdateHappInitEvntWithPatch() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();

        // Update the happInitEvnt using partial update
        HappInitEvnt partialUpdatedHappInitEvnt = new HappInitEvnt();
        partialUpdatedHappInitEvnt.setId(happInitEvnt.getId());

        partialUpdatedHappInitEvnt
            .initEventName(UPDATED_INIT_EVENT_NAME)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .totalTarget(UPDATED_TOTAL_TARGET)
            .notes(UPDATED_NOTES)
            .initOrEvent(UPDATED_INIT_OR_EVENT)
            .addDate(UPDATED_ADD_DATE)
            .addBy(UPDATED_ADD_BY);

        restHappInitEvntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHappInitEvnt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHappInitEvnt))
            )
            .andExpect(status().isOk());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
        HappInitEvnt testHappInitEvnt = happInitEvntList.get(happInitEvntList.size() - 1);
        assertThat(testHappInitEvnt.getInitEventName()).isEqualTo(UPDATED_INIT_EVENT_NAME);
        assertThat(testHappInitEvnt.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testHappInitEvnt.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testHappInitEvnt.getTotalTarget()).isEqualTo(UPDATED_TOTAL_TARGET);
        assertThat(testHappInitEvnt.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHappInitEvnt.getInitOrEvent()).isEqualTo(UPDATED_INIT_OR_EVENT);
        assertThat(testHappInitEvnt.getAddDate()).isEqualTo(UPDATED_ADD_DATE);
        assertThat(testHappInitEvnt.getAddBy()).isEqualTo(UPDATED_ADD_BY);
    }

    @Test
    @Transactional
    void patchNonExistingHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, happInitEvnt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHappInitEvnt() throws Exception {
        int databaseSizeBeforeUpdate = happInitEvntRepository.findAll().size();
        happInitEvnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHappInitEvntMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(happInitEvnt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HappInitEvnt in the database
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHappInitEvnt() throws Exception {
        // Initialize the database
        happInitEvntRepository.saveAndFlush(happInitEvnt);

        int databaseSizeBeforeDelete = happInitEvntRepository.findAll().size();

        // Delete the happInitEvnt
        restHappInitEvntMockMvc
            .perform(delete(ENTITY_API_URL_ID, happInitEvnt.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HappInitEvnt> happInitEvntList = happInitEvntRepository.findAll();
        assertThat(happInitEvntList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
