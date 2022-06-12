package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.Beneficiary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Beneficiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {}
