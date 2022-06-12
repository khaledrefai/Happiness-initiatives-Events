package ae.gov.dubaipolice.happy.web.rest;

import ae.gov.dubaipolice.happy.domain.Beneficiary;
import ae.gov.dubaipolice.happy.repository.BeneficiaryRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ae.gov.dubaipolice.happy.domain.Beneficiary}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BeneficiaryResource {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryResource.class);

    private static final String ENTITY_NAME = "beneficiary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryResource(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    /**
     * {@code POST  /beneficiaries} : Create a new beneficiary.
     *
     * @param beneficiary the beneficiary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiary, or with status {@code 400 (Bad Request)} if the beneficiary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beneficiaries")
    public ResponseEntity<Beneficiary> createBeneficiary(@RequestBody Beneficiary beneficiary) throws URISyntaxException {
        log.debug("REST request to save Beneficiary : {}", beneficiary);
        if (beneficiary.getId() != null) {
            throw new BadRequestAlertException("A new beneficiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Beneficiary result = beneficiaryRepository.save(beneficiary);
        return ResponseEntity
            .created(new URI("/api/beneficiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beneficiaries/:id} : Updates an existing beneficiary.
     *
     * @param id the id of the beneficiary to save.
     * @param beneficiary the beneficiary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiary,
     * or with status {@code 400 (Bad Request)} if the beneficiary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beneficiaries/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Beneficiary beneficiary
    ) throws URISyntaxException {
        log.debug("REST request to update Beneficiary : {}, {}", id, beneficiary);
        if (beneficiary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Beneficiary result = beneficiaryRepository.save(beneficiary);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiary.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beneficiaries/:id} : Partial updates given fields of an existing beneficiary, field will ignore if it is null
     *
     * @param id the id of the beneficiary to save.
     * @param beneficiary the beneficiary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiary,
     * or with status {@code 400 (Bad Request)} if the beneficiary is not valid,
     * or with status {@code 404 (Not Found)} if the beneficiary is not found,
     * or with status {@code 500 (Internal Server Error)} if the beneficiary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beneficiaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Beneficiary> partialUpdateBeneficiary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Beneficiary beneficiary
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beneficiary partially : {}, {}", id, beneficiary);
        if (beneficiary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Beneficiary> result = beneficiaryRepository
            .findById(beneficiary.getId())
            .map(existingBeneficiary -> {
                if (beneficiary.getEid() != null) {
                    existingBeneficiary.setEid(beneficiary.getEid());
                }
                if (beneficiary.getUid() != null) {
                    existingBeneficiary.setUid(beneficiary.getUid());
                }
                if (beneficiary.getFullName() != null) {
                    existingBeneficiary.setFullName(beneficiary.getFullName());
                }
                if (beneficiary.getFullNameEn() != null) {
                    existingBeneficiary.setFullNameEn(beneficiary.getFullNameEn());
                }
                if (beneficiary.getGrpNumber() != null) {
                    existingBeneficiary.setGrpNumber(beneficiary.getGrpNumber());
                }
                if (beneficiary.getNationality() != null) {
                    existingBeneficiary.setNationality(beneficiary.getNationality());
                }
                if (beneficiary.getGenderId() != null) {
                    existingBeneficiary.setGenderId(beneficiary.getGenderId());
                }
                if (beneficiary.getBirthDate() != null) {
                    existingBeneficiary.setBirthDate(beneficiary.getBirthDate());
                }

                return existingBeneficiary;
            })
            .map(beneficiaryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiary.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiaries} : get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaries in body.
     */
    @GetMapping("/beneficiaries")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Beneficiaries");
        Page<Beneficiary> page = beneficiaryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beneficiaries/:id} : get the "id" beneficiary.
     *
     * @param id the id of the beneficiary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beneficiaries/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary(@PathVariable Long id) {
        log.debug("REST request to get Beneficiary : {}", id);
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(beneficiary);
    }

    /**
     * {@code DELETE  /beneficiaries/:id} : delete the "id" beneficiary.
     *
     * @param id the id of the beneficiary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beneficiaries/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        log.debug("REST request to delete Beneficiary : {}", id);
        beneficiaryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
