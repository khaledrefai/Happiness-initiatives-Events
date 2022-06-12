package ae.gov.dubaipolice.happy.service;

import ae.gov.dubaipolice.happy.domain.*; // for static metamodels
import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import ae.gov.dubaipolice.happy.repository.HappInitEvntRepository;
import ae.gov.dubaipolice.happy.service.criteria.HappInitEvntCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HappInitEvnt} entities in the database.
 * The main input is a {@link HappInitEvntCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HappInitEvnt} or a {@link Page} of {@link HappInitEvnt} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HappInitEvntQueryService extends QueryService<HappInitEvnt> {

    private final Logger log = LoggerFactory.getLogger(HappInitEvntQueryService.class);

    private final HappInitEvntRepository happInitEvntRepository;

    public HappInitEvntQueryService(HappInitEvntRepository happInitEvntRepository) {
        this.happInitEvntRepository = happInitEvntRepository;
    }

    /**
     * Return a {@link List} of {@link HappInitEvnt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HappInitEvnt> findByCriteria(HappInitEvntCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HappInitEvnt> specification = createSpecification(criteria);
        return happInitEvntRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HappInitEvnt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HappInitEvnt> findByCriteria(HappInitEvntCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HappInitEvnt> specification = createSpecification(criteria);
        return happInitEvntRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HappInitEvntCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HappInitEvnt> specification = createSpecification(criteria);
        return happInitEvntRepository.count(specification);
    }

    /**
     * Function to convert {@link HappInitEvntCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HappInitEvnt> createSpecification(HappInitEvntCriteria criteria) {
        Specification<HappInitEvnt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HappInitEvnt_.id));
            }
            if (criteria.getInitEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInitEventName(), HappInitEvnt_.initEventName));
            }
            if (criteria.getDateFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFrom(), HappInitEvnt_.dateFrom));
            }
            if (criteria.getDateTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTo(), HappInitEvnt_.dateTo));
            }
            if (criteria.getTotalTarget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalTarget(), HappInitEvnt_.totalTarget));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HappInitEvnt_.notes));
            }
            if (criteria.getInitOrEvent() != null) {
                specification = specification.and(buildSpecification(criteria.getInitOrEvent(), HappInitEvnt_.initOrEvent));
            }
            if (criteria.getAddDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddDate(), HappInitEvnt_.addDate));
            }
            if (criteria.getAddBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddBy(), HappInitEvnt_.addBy));
            }
            if (criteria.getAttachmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttachmentId(),
                            root -> root.join(HappInitEvnt_.attachments, JoinType.LEFT).get(Attachment_.id)
                        )
                    );
            }
            if (criteria.getHappEvntTypId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHappEvntTypId(),
                            root -> root.join(HappInitEvnt_.happEvntTyp, JoinType.LEFT).get(HappEvntTyp_.id)
                        )
                    );
            }
            if (criteria.getHappInitTypId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHappInitTypId(),
                            root -> root.join(HappInitEvnt_.happInitTyp, JoinType.LEFT).get(HappInitTyp_.id)
                        )
                    );
            }
            if (criteria.getHappTargtId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHappTargtId(),
                            root -> root.join(HappInitEvnt_.happTargt, JoinType.LEFT).get(HappTargt_.id)
                        )
                    );
            }
            if (criteria.getHappEvntPlacId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHappEvntPlacId(),
                            root -> root.join(HappInitEvnt_.happEvntPlac, JoinType.LEFT).get(HappEvntPlac_.id)
                        )
                    );
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(HappInitEvnt_.employees, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getBeneficiaryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBeneficiaryId(),
                            root -> root.join(HappInitEvnt_.beneficiaries, JoinType.LEFT).get(Beneficiary_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
