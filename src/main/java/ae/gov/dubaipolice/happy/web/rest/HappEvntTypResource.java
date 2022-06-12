package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.HappEvntTyp;
import ae.gov.dubaipolice.happy.repository.HappEvntTypRepository;
import ae.gov.dubaipolice.happy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.HappEvntTyp}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HappEvntTypResource {

    private final Logger log = LoggerFactory.getLogger(HappEvntTypResource.class);

    private static final String ENTITY_NAME = "happEvntTyp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HappEvntTypRepository happEvntTypRepository;

    public HappEvntTypResource(HappEvntTypRepository happEvntTypRepository) {
        this.happEvntTypRepository = happEvntTypRepository;
    }

    /**
     * {@code POST  /happ-evnt-typs} : Create a new happEvntTyp.
     *
     * @param happEvntTyp the happEvntTyp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new happEvntTyp, or with status {@code 400 (Bad Request)} if the happEvntTyp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/happ-evnt-typs")
    public ResponseEntity<HappEvntTyp> createHappEvntTyp(@Valid @RequestBody HappEvntTyp happEvntTyp) throws URISyntaxException {
        log.debug("REST request to save HappEvntTyp : {}", happEvntTyp);
        if (happEvntTyp.getId() != null) {
            throw new BadRequestAlertException("A new happEvntTyp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HappEvntTyp result = happEvntTypRepository.save(happEvntTyp);
        return ResponseEntity
            .created(new URI("/api/happ-evnt-typs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /happ-evnt-typs/:id} : Updates an existing happEvntTyp.
     *
     * @param id the id of the happEvntTyp to save.
     * @param happEvntTyp the happEvntTyp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happEvntTyp,
     * or with status {@code 400 (Bad Request)} if the happEvntTyp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the happEvntTyp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/happ-evnt-typs/{id}")
    public ResponseEntity<HappEvntTyp> updateHappEvntTyp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HappEvntTyp happEvntTyp
    ) throws URISyntaxException {
        log.debug("REST request to update HappEvntTyp : {}, {}", id, happEvntTyp);
        if (happEvntTyp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happEvntTyp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happEvntTypRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HappEvntTyp result = happEvntTypRepository.save(happEvntTyp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happEvntTyp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /happ-evnt-typs/:id} : Partial updates given fields of an existing happEvntTyp, field will ignore if it is null
     *
     * @param id the id of the happEvntTyp to save.
     * @param happEvntTyp the happEvntTyp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happEvntTyp,
     * or with status {@code 400 (Bad Request)} if the happEvntTyp is not valid,
     * or with status {@code 404 (Not Found)} if the happEvntTyp is not found,
     * or with status {@code 500 (Internal Server Error)} if the happEvntTyp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/happ-evnt-typs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HappEvntTyp> partialUpdateHappEvntTyp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HappEvntTyp happEvntTyp
    ) throws URISyntaxException {
        log.debug("REST request to partial update HappEvntTyp partially : {}, {}", id, happEvntTyp);
        if (happEvntTyp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happEvntTyp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happEvntTypRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HappEvntTyp> result = happEvntTypRepository
            .findById(happEvntTyp.getId())
            .map(existingHappEvntTyp -> {
                if (happEvntTyp.getEventName() != null) {
                    existingHappEvntTyp.setEventName(happEvntTyp.getEventName());
                }

                return existingHappEvntTyp;
            })
            .map(happEvntTypRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happEvntTyp.getId().toString())
        );
    }

    /**
     * {@code GET  /happ-evnt-typs} : get all the happEvntTyps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of happEvntTyps in body.
     */
    @GetMapping("/happ-evnt-typs")
    public List<HappEvntTyp> getAllHappEvntTyps() {
        log.debug("REST request to get all HappEvntTyps");
        return happEvntTypRepository.findAll();
    }

    /**
     * {@code GET  /happ-evnt-typs/:id} : get the "id" happEvntTyp.
     *
     * @param id the id of the happEvntTyp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the happEvntTyp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/happ-evnt-typs/{id}")
    public ResponseEntity<HappEvntTyp> getHappEvntTyp(@PathVariable Long id) {
        log.debug("REST request to get HappEvntTyp : {}", id);
        Optional<HappEvntTyp> happEvntTyp = happEvntTypRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(happEvntTyp);
    }

    /**
     * {@code DELETE  /happ-evnt-typs/:id} : delete the "id" happEvntTyp.
     *
     * @param id the id of the happEvntTyp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/happ-evnt-typs/{id}")
    public ResponseEntity<Void> deleteHappEvntTyp(@PathVariable Long id) {
        log.debug("REST request to delete HappEvntTyp : {}", id);
        happEvntTypRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
