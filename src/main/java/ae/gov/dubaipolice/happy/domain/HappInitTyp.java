package ae.gov.dubaipolice.happy.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HappInitTyp.
 */
@Entity
@Table(name = "happ_init_typ")
public class HappInitTyp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "init_name", nullable = false)
    private String initName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HappInitTyp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitName() {
        return this.initName;
    }

    public HappInitTyp initName(String initName) {
        this.setInitName(initName);
        return this;
    }

    public void setInitName(String initName) {
        this.initName = initName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HappInitTyp)) {
            return false;
        }
        return id != null && id.equals(((HappInitTyp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HappInitTyp{" +
            "id=" + getId() +
            ", initName='" + getInitName() + "'" +
            "}";
    }
}
