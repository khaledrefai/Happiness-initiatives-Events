package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappEvntPlac;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HappEvntPlac entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HappEvntPlacRepository extends JpaRepository<HappEvntPlac, Long> {}
