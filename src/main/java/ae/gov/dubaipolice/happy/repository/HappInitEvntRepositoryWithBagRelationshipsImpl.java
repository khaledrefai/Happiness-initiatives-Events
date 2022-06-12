package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class HappInitEvntRepositoryWithBagRelationshipsImpl implements HappInitEvntRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<HappInitEvnt> fetchBagRelationships(Optional<HappInitEvnt> happInitEvnt) {
        return happInitEvnt.map(this::fetchEmployees).map(this::fetchBeneficiaries);
    }

    @Override
    public Page<HappInitEvnt> fetchBagRelationships(Page<HappInitEvnt> happInitEvnts) {
        return new PageImpl<>(
            fetchBagRelationships(happInitEvnts.getContent()),
            happInitEvnts.getPageable(),
            happInitEvnts.getTotalElements()
        );
    }

    @Override
    public List<HappInitEvnt> fetchBagRelationships(List<HappInitEvnt> happInitEvnts) {
        return Optional.of(happInitEvnts).map(this::fetchEmployees).map(this::fetchBeneficiaries).orElse(Collections.emptyList());
    }

    HappInitEvnt fetchEmployees(HappInitEvnt result) {
        return entityManager
            .createQuery(
                "select happInitEvnt from HappInitEvnt happInitEvnt left join fetch happInitEvnt.employees where happInitEvnt is :happInitEvnt",
                HappInitEvnt.class
            )
            .setParameter("happInitEvnt", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<HappInitEvnt> fetchEmployees(List<HappInitEvnt> happInitEvnts) {
        return entityManager
            .createQuery(
                "select distinct happInitEvnt from HappInitEvnt happInitEvnt left join fetch happInitEvnt.employees where happInitEvnt in :happInitEvnts",
                HappInitEvnt.class
            )
            .setParameter("happInitEvnts", happInitEvnts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    HappInitEvnt fetchBeneficiaries(HappInitEvnt result) {
        return entityManager
            .createQuery(
                "select happInitEvnt from HappInitEvnt happInitEvnt left join fetch happInitEvnt.beneficiaries where happInitEvnt is :happInitEvnt",
                HappInitEvnt.class
            )
            .setParameter("happInitEvnt", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<HappInitEvnt> fetchBeneficiaries(List<HappInitEvnt> happInitEvnts) {
        return entityManager
            .createQuery(
                "select distinct happInitEvnt from HappInitEvnt happInitEvnt left join fetch happInitEvnt.beneficiaries where happInitEvnt in :happInitEvnts",
                HappInitEvnt.class
            )
            .setParameter("happInitEvnts", happInitEvnts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
