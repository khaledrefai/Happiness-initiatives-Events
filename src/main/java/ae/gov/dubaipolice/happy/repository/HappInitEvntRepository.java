package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HappInitEvnt entity.
 */
@Repository
public interface HappInitEvntRepository
    extends HappInitEvntRepositoryWithBagRelationships, JpaRepository<HappInitEvnt, Long>, JpaSpecificationExecutor<HappInitEvnt> {
    default Optional<HappInitEvnt> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<HappInitEvnt> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<HappInitEvnt> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
