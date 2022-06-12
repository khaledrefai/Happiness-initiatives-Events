package ae.gov.dubaipolice.happy.service;

import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import ae.gov.dubaipolice.happy.repository.HappInitEvntRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HappInitEvnt}.
 */
@Service
@Transactional
public class HappInitEvntService {

    private final Logger log = LoggerFactory.getLogger(HappInitEvntService.class);

    private final HappInitEvntRepository happInitEvntRepository;

    public HappInitEvntService(HappInitEvntRepository happInitEvntRepository) {
        this.happInitEvntRepository = happInitEvntRepository;
    }

    /**
     * Save a happInitEvnt.
     *
     * @param happInitEvnt the entity to save.
     * @return the persisted entity.
     */
    public HappInitEvnt save(HappInitEvnt happInitEvnt) {
        log.debug("Request to save HappInitEvnt : {}", happInitEvnt);
        return happInitEvntRepository.save(happInitEvnt);
    }

    /**
     * Update a happInitEvnt.
     *
     * @param happInitEvnt the entity to save.
     * @return the persisted entity.
     */
    public HappInitEvnt update(HappInitEvnt happInitEvnt) {
        log.debug("Request to save HappInitEvnt : {}", happInitEvnt);
        return happInitEvntRepository.save(happInitEvnt);
    }

    /**
     * Partially update a happInitEvnt.
     *
     * @param happInitEvnt the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HappInitEvnt> partialUpdate(HappInitEvnt happInitEvnt) {
        log.debug("Request to partially update HappInitEvnt : {}", happInitEvnt);

        return happInitEvntRepository
            .findById(happInitEvnt.getId())
            .map(existingHappInitEvnt -> {
                if (happInitEvnt.getInitEventName() != null) {
                    existingHappInitEvnt.setInitEventName(happInitEvnt.getInitEventName());
                }
                if (happInitEvnt.getDateFrom() != null) {
                    existingHappInitEvnt.setDateFrom(happInitEvnt.getDateFrom());
                }
                if (happInitEvnt.getDateTo() != null) {
                    existingHappInitEvnt.setDateTo(happInitEvnt.getDateTo());
                }
                if (happInitEvnt.getTotalTarget() != null) {
                    existingHappInitEvnt.setTotalTarget(happInitEvnt.getTotalTarget());
                }
                if (happInitEvnt.getNotes() != null) {
                    existingHappInitEvnt.setNotes(happInitEvnt.getNotes());
                }
                if (happInitEvnt.getInitOrEvent() != null) {
                    existingHappInitEvnt.setInitOrEvent(happInitEvnt.getInitOrEvent());
                }
                if (happInitEvnt.getAddDate() != null) {
                    existingHappInitEvnt.setAddDate(happInitEvnt.getAddDate());
                }
                if (happInitEvnt.getAddBy() != null) {
                    existingHappInitEvnt.setAddBy(happInitEvnt.getAddBy());
                }

                return existingHappInitEvnt;
            })
            .map(happInitEvntRepository::save);
    }

    /**
     * Get all the happInitEvnts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HappInitEvnt> findAll(Pageable pageable) {
        log.debug("Request to get all HappInitEvnts");
        return happInitEvntRepository.findAll(pageable);
    }

    /**
     * Get all the happInitEvnts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HappInitEvnt> findAllWithEagerRelationships(Pageable pageable) {
        return happInitEvntRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one happInitEvnt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HappInitEvnt> findOne(Long id) {
        log.debug("Request to get HappInitEvnt : {}", id);
        return happInitEvntRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the happInitEvnt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HappInitEvnt : {}", id);
        happInitEvntRepository.deleteById(id);
    }
}
