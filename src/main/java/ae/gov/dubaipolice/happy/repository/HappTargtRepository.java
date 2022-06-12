package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappTargt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HappTargt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HappTargtRepository extends JpaRepository<HappTargt, Long> {}
