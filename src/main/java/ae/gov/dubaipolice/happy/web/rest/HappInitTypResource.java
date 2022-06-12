package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.HappInitTyp;
import ae.gov.dubaipolice.happy.repository.HappInitTypRepository;
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
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.HappInitTyp}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HappInitTypResource {

    private final Logger log = LoggerFactory.getLogger(HappInitTypResource.class);

    private static final String ENTITY_NAME = "happInitTyp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HappInitTypRepository happInitTypRepository;

    public HappInitTypResource(HappInitTypRepository happInitTypRepository) {
        this.happInitTypRepository = happInitTypRepository;
    }

    /**
     * {@code POST  /happ-init-typs} : Create a new happInitTyp.
     *
     * @param happInitTyp the happInitTyp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new happInitTyp, or with status {@code 400 (Bad Request)} if the happInitTyp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/happ-init-typs")
    public ResponseEntity<HappInitTyp> createHappInitTyp(@Valid @RequestBody HappInitTyp happInitTyp) throws URISyntaxException {
        log.debug("REST request to save HappInitTyp : {}", happInitTyp);
        if (happInitTyp.getId() != null) {
            throw new BadRequestAlertException("A new happInitTyp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HappInitTyp result = happInitTypRepository.save(happInitTyp);
        return ResponseEntity
            .created(new URI("/api/happ-init-typs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /happ-init-typs/:id} : Updates an existing happInitTyp.
     *
     * @param id the id of the happInitTyp to save.
     * @param happInitTyp the happInitTyp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happInitTyp,
     * or with status {@code 400 (Bad Request)} if the happInitTyp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the happInitTyp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/happ-init-typs/{id}")
    public ResponseEntity<HappInitTyp> updateHappInitTyp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HappInitTyp happInitTyp
    ) throws URISyntaxException {
        log.debug("REST request to update HappInitTyp : {}, {}", id, happInitTyp);
        if (happInitTyp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happInitTyp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happInitTypRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HappInitTyp result = happInitTypRepository.save(happInitTyp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happInitTyp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /happ-init-typs/:id} : Partial updates given fields of an existing happInitTyp, field will ignore if it is null
     *
     * @param id the id of the happInitTyp to save.
     * @param happInitTyp the happInitTyp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated happInitTyp,
     * or with status {@code 400 (Bad Request)} if the happInitTyp is not valid,
     * or with status {@code 404 (Not Found)} if the happInitTyp is not found,
     * or with status {@code 500 (Internal Server Error)} if the happInitTyp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/happ-init-typs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HappInitTyp> partialUpdateHappInitTyp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HappInitTyp happInitTyp
    ) throws URISyntaxException {
        log.debug("REST request to partial update HappInitTyp partially : {}, {}", id, happInitTyp);
        if (happInitTyp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, happInitTyp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!happInitTypRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HappInitTyp> result = happInitTypRepository
            .findById(happInitTyp.getId())
            .map(existingHappInitTyp -> {
                if (happInitTyp.getInitName() != null) {
                    existingHappInitTyp.setInitName(happInitTyp.getInitName());
                }

                return existingHappInitTyp;
            })
            .map(happInitTypRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, happInitTyp.getId().toString())
        );
    }

    /**
     * {@code GET  /happ-init-typs} : get all the happInitTyps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of happInitTyps in body.
     */
    @GetMapping("/happ-init-typs")
    public List<HappInitTyp> getAllHappInitTyps() {
        log.debug("REST request to get all HappInitTyps");
        return happInitTypRepository.findAll();
    }

    /**
     * {@code GET  /happ-init-typs/:id} : get the "id" happInitTyp.
     *
     * @param id the id of the happInitTyp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the happInitTyp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/happ-init-typs/{id}")
    public ResponseEntity<HappInitTyp> getHappInitTyp(@PathVariable Long id) {
        log.debug("REST request to get HappInitTyp : {}", id);
        Optional<HappInitTyp> happInitTyp = happInitTypRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(happInitTyp);
    }

    /**
     * {@code DELETE  /happ-init-typs/:id} : delete the "id" happInitTyp.
     *
     * @param id the id of the happInitTyp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/happ-init-typs/{id}")
    public ResponseEntity<Void> deleteHappInitTyp(@PathVariable Long id) {
        log.debug("REST request to delete HappInitTyp : {}", id);
        happInitTypRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
