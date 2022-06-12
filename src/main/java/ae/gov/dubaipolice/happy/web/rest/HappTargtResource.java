package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.HappTargt;
import ae.gov.dubaipolice.happy.repository.HappTargtRepository;
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
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.HappTargt}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HappTargtResource {

    private final Logger log = LoggerFactory.getLogger(HappTargtResource.class);

    private static final String ENTITY_NAME = "happTargt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HappTargtRepository happTargtRepository;

    public HappTargtResource(HappTargtRepository happTargtRepository) {
        this.happTargtRepository = happTargtRepository;
    }

    /**
     * {@code POST  /happ-targts} : Create a new happTargt.
     *
     * @param happTargt the happTargt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new happTargt, or with status {@code 400 (Bad Request)} if the happTargt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/happ-targts")
    public ResponseEntity<HappTargt> createHappTargt(@Valid @RequestBody HappTargt happTargt) throws URISyntaxException {
        log.debug("REST request to save HappTargt : {}", happTargt);
        if (happTargt.getId() != null) {
            throw new BadRequestAlertException("A new happTargt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HappTargt result = happTargtRepository.save(happTargt);
        return ResponseEntity
            .created(new URI("/api/happ-targts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /happ-targts/:id} : Updates an existing happTargt.
     *
     * @param id the id of the happTargt to save.
     * @param happTargt the happTargt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happTargt,
     * or with status {@code 400 (Bad Request)} if the happTargt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the happTargt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/happ-targts/{id}")
    public ResponseEntity<HappTargt> updateHappTargt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HappTargt happTargt
    ) throws URISyntaxException {
        log.debug("REST request to update HappTargt : {}, {}", id, happTargt);
        if (happTargt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happTargt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happTargtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HappTargt result = happTargtRepository.save(happTargt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happTargt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /happ-targts/:id} : Partial updates given fields of an existing happTargt, field will ignore if it is null
     *
     * @param id the id of the happTargt to save.
     * @param happTargt the happTargt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happTargt,
     * or with status {@code 400 (Bad Request)} if the happTargt is not valid,
     * or with status {@code 404 (Not Found)} if the happTargt is not found,
     * or with status {@code 500 (Internal Server Error)} if the happTargt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/happ-targts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HappTargt> partialUpdateHappTargt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HappTargt happTargt
    ) throws URISyntaxException {
        log.debug("REST request to partial update HappTargt partially : {}, {}", id, happTargt);
        if (happTargt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happTargt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happTargtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HappTargt> result = happTargtRepository
            .findById(happTargt.getId())
            .map(existingHappTargt -> {
                if (happTargt.getTargetName() != null) {
                    existingHappTargt.setTargetName(happTargt.getTargetName());
                }

                return existingHappTargt;
            })
            .map(happTargtRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happTargt.getId().toString())
        );
    }

    /**
     * {@code GET  /happ-targts} : get all the happTargts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of happTargts in body.
     */
    @GetMapping("/happ-targts")
    public List<HappTargt> getAllHappTargts() {
        log.debug("REST request to get all HappTargts");
        return happTargtRepository.findAll();
    }

    /**
     * {@code GET  /happ-targts/:id} : get the "id" happTargt.
     *
     * @param id the id of the happTargt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the happTargt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/happ-targts/{id}")
    public ResponseEntity<HappTargt> getHappTargt(@PathVariable Long id) {
        log.debug("REST request to get HappTargt : {}", id);
        Optional<HappTargt> happTargt = happTargtRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(happTargt);
    }

    /**
     * {@code DELETE  /happ-targts/:id} : delete the "id" happTargt.
     *
     * @param id the id of the happTargt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/happ-targts/{id}")
    public ResponseEntity<Void> deleteHappTargt(@PathVariable Long id) {
        log.debug("REST request to delete HappTargt : {}", id);
        happTargtRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
