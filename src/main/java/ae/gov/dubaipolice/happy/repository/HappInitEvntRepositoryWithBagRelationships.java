package ae.gov.dubaipolice.happy.repository;

import ae.gov.dubaipolice.happy.domain.HappInitEvnt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HappInitEvntRepositoryWithBagRelationships {
    Optional<HappInitEvnt> fetchBagRelationships(Optional<HappInitEvnt> happInitEvnt);

    List<HappInitEvnt> fetchBagRelationships(List<HappInitEvnt> happInitEvnts);

    Page<HappInitEvnt> fetchBagRelationships(Page<HappInitEvnt> happInitEvnts);
}
