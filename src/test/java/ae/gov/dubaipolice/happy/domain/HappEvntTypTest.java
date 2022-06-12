package ae.gov.dubaipolice.happy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ae.gov.dubaipolice.happy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HappEvntTypTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HappEvntTyp.class);
        HappEvntTyp happEvntTyp1 = new HappEvntTyp();
        happEvntTyp1.setId(1L);
        HappEvntTyp happEvntTyp2 = new HappEvntTyp();
        happEvntTyp2.setId(happEvntTyp1.getId());
        assertThat(happEvntTyp1).isEqualTo(happEvntTyp2);
        happEvntTyp2.setId(2L);
        assertThat(happEvntTyp1).isNotEqualTo(happEvntTyp2);
        happEvntTyp1.setId(null);
        assertThat(happEvntTyp1).isNotEqualTo(happEvntTyp2);
    }
}
