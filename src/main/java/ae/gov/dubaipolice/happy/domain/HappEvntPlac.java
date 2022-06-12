package ae.gov.dubaipolice.happy.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HappEvntPlac.
 */
@Entity
@Table(name = "happ_evnt_plac")
public class HappEvntPlac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "place_name", nullable = false)
    private String placeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HappEvntPlac id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public HappEvntPlac placeName(String placeName) {
        this.setPlaceName(placeName);
        return this;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HappEvntPlac)) {
            return false;
        }
        return id != null && id.equals(((HappEvntPlac) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HappEvntPlac{" +
            "id=" + getId() +
            ", placeName='" + getPlaceName() + "'" +
            "}";
    }
}
