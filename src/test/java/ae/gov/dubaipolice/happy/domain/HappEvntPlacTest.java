package ae.gov.dubaipolice.happy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ae.gov.dubaipolice.happy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HappEvntPlacTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HappEvntPlac.class);
        HappEvntPlac happEvntPlac1 = new HappEvntPlac();
        happEvntPlac1.setId(1L);
        HappEvntPlac happEvntPlac2 = new HappEvntPlac();
        happEvntPlac2.setId(happEvntPlac1.getId());
        assertThat(happEvntPlac1).isEqualTo(happEvntPlac2);
        happEvntPlac2.setId(2L);
        assertThat(happEvntPlac1).isNotEqualTo(happEvntPlac2);
        happEvntPlac1.setId(null);
        assertThat(happEvntPlac1).isNotEqualTo(happEvntPlac2);
    }
}
