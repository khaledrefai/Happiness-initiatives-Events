package ae.gov.dubaipolice.happy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ae.gov.dubaipolice.happy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HappTargtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HappTargt.class);
        HappTargt happTargt1 = new HappTargt();
        happTargt1.setId(1L);
        HappTargt happTargt2 = new HappTargt();
        happTargt2.setId(happTargt1.getId());
        assertThat(happTargt1).isEqualTo(happTargt2);
        happTargt2.setId(2L);
        assertThat(happTargt1).isNotEqualTo(happTargt2);
        happTargt1.setId(null);
        assertThat(happTargt1).isNotEqualTo(happTargt2);
    }
}
