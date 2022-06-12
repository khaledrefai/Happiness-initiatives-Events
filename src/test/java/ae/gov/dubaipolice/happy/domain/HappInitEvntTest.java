package ae.gov.dubaipolice.happy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ae.gov.dubaipolice.happy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HappInitEvntTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HappInitEvnt.class);
        HappInitEvnt happInitEvnt1 = new HappInitEvnt();
        happInitEvnt1.setId(1L);
        HappInitEvnt happInitEvnt2 = new HappInitEvnt();
        happInitEvnt2.setId(happInitEvnt1.getId());
        assertThat(happInitEvnt1).isEqualTo(happInitEvnt2);
        happInitEvnt2.setId(2L);
        assertThat(happInitEvnt1).isNotEqualTo(happInitEvnt2);
        happInitEvnt1.setId(null);
        assertThat(happInitEvnt1).isNotEqualTo(happInitEvnt2);
    }
}
