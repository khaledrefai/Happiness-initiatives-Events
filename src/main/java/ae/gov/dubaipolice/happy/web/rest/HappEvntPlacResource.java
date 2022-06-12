package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.HappEvntPlac;
import ae.gov.dubaipolice.happy.repository.HappEvntPlacRepository;
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
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.HappEvntPlac}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HappEvntPlacResource {

    private final Logger log = LoggerFactory.getLogger(HappEvntPlacResource.class);

    private static final String ENTITY_NAME = "happEvntPlac";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HappEvntPlacRepository happEvntPlacRepository;

    public HappEvntPlacResource(HappEvntPlacRepository happEvntPlacRepository) {
        this.happEvntPlacRepository = happEvntPlacRepository;
    }

    /**
     * {@code POST  /happ-evnt-placs} : Create a new happEvntPlac.
     *
     * @param happEvntPlac the happEvntPlac to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new happEvntPlac, or with status {@code 400 (Bad Request)} if the happEvntPlac has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/happ-evnt-placs")
    public ResponseEntity<HappEvntPlac> createHappEvntPlac(@Valid @RequestBody HappEvntPlac happEvntPlac) throws URISyntaxException {
        log.debug("REST request to save HappEvntPlac : {}", happEvntPlac);
        if (happEvntPlac.getId() != null) {
            throw new BadRequestAlertException("A new happEvntPlac cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HappEvntPlac result = happEvntPlacRepository.save(happEvntPlac);
        return ResponseEntity
            .created(new URI("/api/happ-evnt-placs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /happ-evnt-placs/:id} : Updates an existing happEvntPlac.
     *
     * @param id the id of the happEvntPlac to save.
     * @param happEvntPlac the happEvntPlac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happEvntPlac,
     * or with status {@code 400 (Bad Request)} if the happEvntPlac is not valid,
     * or with status {@code 500 (Internal Server Error)} if the happEvntPlac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/happ-evnt-placs/{id}")
    public ResponseEntity<HappEvntPlac> updateHappEvntPlac(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HappEvntPlac happEvntPlac
    ) throws URISyntaxException {
        log.debug("REST request to update HappEvntPlac : {}, {}", id, happEvntPlac);
        if (happEvntPlac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happEvntPlac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happEvntPlacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HappEvntPlac result = happEvntPlacRepository.save(happEvntPlac);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happEvntPlac.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /happ-evnt-placs/:id} : Partial updates given fields of an existing happEvntPlac, field will ignore if it is null
     *
     * @param id the id of the happEvntPlac to save.
     * @param happEvntPlac the happEvntPlac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happEvntPlac,
     * or with status {@code 400 (Bad Request)} if the happEvntPlac is not valid,
     * or with status {@code 404 (Not Found)} if the happEvntPlac is not found,
     * or with status {@code 500 (Internal Server Error)} if the happEvntPlac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/happ-evnt-placs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HappEvntPlac> partialUpdateHappEvntPlac(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HappEvntPlac happEvntPlac
    ) throws URISyntaxException {
        log.debug("REST request to partial update HappEvntPlac partially : {}, {}", id, happEvntPlac);
        if (happEvntPlac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happEvntPlac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happEvntPlacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HappEvntPlac> result = happEvntPlacRepository
            .findById(happEvntPlac.getId())
            .map(existingHappEvntPlac -> {
                if (happEvntPlac.getPlaceName() != null) {
                    existingHappEvntPlac.setPlaceName(happEvntPlac.getPlaceName());
                }

                return existingHappEvntPlac;
            })
            .map(happEvntPlacRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happEvntPlac.getId().toString())
        );
    }

    /**
     * {@code GET  /happ-evnt-placs} : get all the happEvntPlacs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of happEvntPlacs in body.
     */
    @GetMapping("/happ-evnt-placs")
    public List<HappEvntPlac> getAllHappEvntPlacs() {
        log.debug("REST request to get all HappEvntPlacs");
        return happEvntPlacRepository.findAll();
    }

    /**
     * {@code GET  /happ-evnt-placs/:id} : get the "id" happEvntPlac.
     *
     * @param id the id of the happEvntPlac to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the happEvntPlac, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/happ-evnt-placs/{id}")
    public ResponseEntity<HappEvntPlac> getHappEvntPlac(@PathVariable Long id) {
        log.debug("REST request to get HappEvntPlac : {}", id);
        Optional<HappEvntPlac> happEvntPlac = happEvntPlacRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(happEvntPlac);
    }

    /**
     * {@code DELETE  /happ-evnt-placs/:id} : delete the "id" happEvntPlac.
     *
     * @param id the id of the happEvntPlac to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/happ-evnt-placs/{id}")
    public ResponseEntity<Void> deleteHappEvntPlac(@PathVariable Long id) {
        log.debug("REST request to delete HappEvntPlac : {}", id);
        happEvntPlacRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
