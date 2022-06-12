package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappInitTyp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HappInitTyp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HappInitTypRepository extends JpaRepository<HappInitTyp, Long> {}
