package ae.gov.dubaipolice.happy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "slm_name")
    private String slmName;

    @ManyToOne
    private Department department;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnoreProperties(
        value = { "attachments", "happEvntTyp", "happInitTyp", "happTargt", "happEvntPlac", "employees", "beneficiaries" },
        allowSetters = true
    )
    private Set<HappInitEvnt> happInitEvnts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlmName() {
        return this.slmName;
    }

    public Employee slmName(String slmName) {
        this.setSlmName(slmName);
        return this;
    }

    public void setSlmName(String slmName) {
        this.slmName = slmName;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Set<HappInitEvnt> getHappInitEvnts() {
        return this.happInitEvnts;
    }

    public void setHappInitEvnts(Set<HappInitEvnt> happInitEvnts) {
        if (this.happInitEvnts != null) {
            this.happInitEvnts.forEach(i -> i.removeEmployee(this));
        }
        if (happInitEvnts != null) {
            happInitEvnts.forEach(i -> i.addEmployee(this));
        }
        this.happInitEvnts = happInitEvnts;
    }

    public Employee happInitEvnts(Set<HappInitEvnt> happInitEvnts) {
        this.setHappInitEvnts(happInitEvnts);
        return this;
    }

    public Employee addHappInitEvnt(HappInitEvnt happInitEvnt) {
        this.happInitEvnts.add(happInitEvnt);
        happInitEvnt.getEmployees().add(this);
        return this;
    }

    public Employee removeHappInitEvnt(HappInitEvnt happInitEvnt) {
        this.happInitEvnts.remove(happInitEvnt);
        happInitEvnt.getEmployees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", slmName='" + getSlmName() + "'" +
            "}";
    }
}
