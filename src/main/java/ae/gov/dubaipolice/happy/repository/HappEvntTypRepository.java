package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappEvntTyp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HappEvntTyp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HappEvntTypRepository extends JpaRepository<HappEvntTyp, Long> {}
