package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import ae.gov.dubaipolice.happy.repository.HappInitEvntRepository;
import ae.gov.dubaipolice.happy.service.HappInitEvntQueryService;
import ae.gov.dubaipolice.happy.service.HappInitEvntService;
import ae.gov.dubaipolice.happy.service.criteria.HappInitEvntCriteria;
import ae.gov.dubaipolice.happy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.HappInitEvnt}.
 */
@RestController
@RequestMapping("/api")
public class HappInitEvntResource {

    private final Logger log = LoggerFactory.getLogger(HappInitEvntResource.class);

    private static final String ENTITY_NAME = "happInitEvnt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HappInitEvntService happInitEvntService;

    private final HappInitEvntRepository happInitEvntRepository;

    private final HappInitEvntQueryService happInitEvntQueryService;

    public HappInitEvntResource(
        HappInitEvntService happInitEvntService,
        HappInitEvntRepository happInitEvntRepository,
        HappInitEvntQueryService happInitEvntQueryService
    ) {
        this.happInitEvntService = happInitEvntService;
        this.happInitEvntRepository = happInitEvntRepository;
        this.happInitEvntQueryService = happInitEvntQueryService;
    }

    /**
     * {@code POST  /happ-init-evnts} : Create a new happInitEvnt.
     *
     * @param happInitEvnt the happInitEvnt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new happInitEvnt, or with status {@code 400 (Bad Request)} if the happInitEvnt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/happ-init-evnts")
    public ResponseEntity<HappInitEvnt> createHappInitEvnt(@RequestBody HappInitEvnt happInitEvnt) throws URISyntaxException {
        log.debug("REST request to save HappInitEvnt : {}", happInitEvnt);
        if (happInitEvnt.getId() != null) {
            throw new BadRequestAlertException("A new happInitEvnt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HappInitEvnt result = happInitEvntService.save(happInitEvnt);
        return ResponseEntity
            .created(new URI("/api/happ-init-evnts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /happ-init-evnts/:id} : Updates an existing happInitEvnt.
     *
     * @param id the id of the happInitEvnt to save.
     * @param happInitEvnt the happInitEvnt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happInitEvnt,
     * or with status {@code 400 (Bad Request)} if the happInitEvnt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the happInitEvnt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/happ-init-evnts/{id}")
    public ResponseEntity<HappInitEvnt> updateHappInitEvnt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HappInitEvnt happInitEvnt
    ) throws URISyntaxException {
        log.debug("REST request to update HappInitEvnt : {}, {}", id, happInitEvnt);
        if (happInitEvnt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happInitEvnt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happInitEvntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HappInitEvnt result = happInitEvntService.update(happInitEvnt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happInitEvnt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /happ-init-evnts/:id} : Partial updates given fields of an existing happInitEvnt, field will ignore if it is null
     *
     * @param id the id of the happInitEvnt to save.
     * @param happInitEvnt the happInitEvnt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happInitEvnt,
     * or with status {@code 400 (Bad Request)} if the happInitEvnt is not valid,
     * or with status {@code 404 (Not Found)} if the happInitEvnt is not found,
     * or with status {@code 500 (Internal Server Error)} if the happInitEvnt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/happ-init-evnts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HappInitEvnt> partialUpdateHappInitEvnt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HappInitEvnt happInitEvnt
    ) throws URISyntaxException {
        log.debug("REST request to partial update HappInitEvnt partially : {}, {}", id, happInitEvnt);
        if (happInitEvnt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happInitEvnt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happInitEvntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HappInitEvnt> result = happInitEvntService.partialUpdate(happInitEvnt);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happInitEvnt.getId().toString())
        );
    }

    /**
     * {@code GET  /happ-init-evnts} : get all the happInitEvnts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of happInitEvnts in body.
     */
    @GetMapping("/happ-init-evnts")
    public ResponseEntity<List<HappInitEvnt>> getAllHappInitEvnts(
        HappInitEvntCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HappInitEvnts by criteria: {}", criteria);
        Page<HappInitEvnt> page = happInitEvntQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /happ-init-evnts/count} : count all the happInitEvnts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/happ-init-evnts/count")
    public ResponseEntity<Long> countHappInitEvnts(HappInitEvntCriteria criteria) {
        log.debug("REST request to count HappInitEvnts by criteria: {}", criteria);
        return ResponseEntity.ok().body(happInitEvntQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /happ-init-evnts/:id} : get the "id" happInitEvnt.
     *
     * @param id the id of the happInitEvnt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the happInitEvnt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/happ-init-evnts/{id}")
    public ResponseEntity<HappInitEvnt> getHappInitEvnt(@PathVariable Long id) {
        log.debug("REST request to get HappInitEvnt : {}", id);
        Optional<HappInitEvnt> happInitEvnt = happInitEvntService.findOne(id);
        return ResponseUtil.wrapOrNotFound(happInitEvnt);
    }

    /**
     * {@code DELETE  /happ-init-evnts/:id} : delete the "id" happInitEvnt.
     *
     * @param id the id of the happInitEvnt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/happ-init-evnts/{id}")
    public ResponseEntity<Void> deleteHappInitEvnt(@PathVariable Long id) {
        log.debug("REST request to delete HappInitEvnt : {}", id);
        happInitEvntService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
