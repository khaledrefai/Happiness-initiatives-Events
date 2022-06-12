package ae.gov.dubaipolice.happy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ae.gov.dubaipolice.happy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HappInitTypTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HappInitTyp.class);
        HappInitTyp happInitTyp1 = new HappInitTyp();
        happInitTyp1.setId(1L);
        HappInitTyp happInitTyp2 = new HappInitTyp();
        happInitTyp2.setId(happInitTyp1.getId());
        assertThat(happInitTyp1).isEqualTo(happInitTyp2);
        happInitTyp2.setId(2L);
        assertThat(happInitTyp1).isNotEqualTo(happInitTyp2);
        happInitTyp1.setId(null);
        assertThat(happInitTyp1).isNotEqualTo(happInitTyp2);
    }
}
