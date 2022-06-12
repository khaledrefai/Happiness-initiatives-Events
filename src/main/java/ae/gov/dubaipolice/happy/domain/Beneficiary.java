package ae.gov.dubaipolice.happy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Beneficiary.
 */
@Entity
@Table(name = "beneficiary")
public class Beneficiary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "eid")
    private String eid;

    @Column(name = "jhi_uid")
    private String uid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "grp_number")
    private Long grpNumber;

    @Column(name = "nationality")
    private Long nationality;

    @Column(name = "gender_id")
    private Integer genderId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "beneficiaries")
    @JsonIgnoreProperties(
        value = { "attachments", "happEvntTyp", "happInitTyp", "happTargt", "happEvntPlac", "employees", "beneficiaries" },
        allowSetters = true
    )
    private Set<HappInitEvnt> happInitEvnts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Beneficiary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEid() {
        return this.eid;
    }

    public Beneficiary eid(String eid) {
        this.setEid(eid);
        return this;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getUid() {
        return this.uid;
    }

    public Beneficiary uid(String uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Beneficiary fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameEn() {
        return this.fullNameEn;
    }

    public Beneficiary fullNameEn(String fullNameEn) {
        this.setFullNameEn(fullNameEn);
        return this;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public Long getGrpNumber() {
        return this.grpNumber;
    }

    public Beneficiary grpNumber(Long grpNumber) {
        this.setGrpNumber(grpNumber);
        return this;
    }

    public void setGrpNumber(Long grpNumber) {
        this.grpNumber = grpNumber;
    }

    public Long getNationality() {
        return this.nationality;
    }

    public Beneficiary nationality(Long nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(Long nationality) {
        this.nationality = nationality;
    }

    public Integer getGenderId() {
        return this.genderId;
    }

    public Beneficiary genderId(Integer genderId) {
        this.setGenderId(genderId);
        return this;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Beneficiary birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<HappInitEvnt> getHappInitEvnts() {
        return this.happInitEvnts;
    }

    public void setHappInitEvnts(Set<HappInitEvnt> happInitEvnts) {
        if (this.happInitEvnts != null) {
            this.happInitEvnts.forEach(i -> i.removeBeneficiary(this));
        }
        if (happInitEvnts != null) {
            happInitEvnts.forEach(i -> i.addBeneficiary(this));
        }
        this.happInitEvnts = happInitEvnts;
    }

    public Beneficiary happInitEvnts(Set<HappInitEvnt> happInitEvnts) {
        this.setHappInitEvnts(happInitEvnts);
        return this;
    }

    public Beneficiary addHappInitEvnt(HappInitEvnt happInitEvnt) {
        this.happInitEvnts.add(happInitEvnt);
        happInitEvnt.getBeneficiaries().add(this);
        return this;
    }

    public Beneficiary removeHappInitEvnt(HappInitEvnt happInitEvnt) {
        this.happInitEvnts.remove(happInitEvnt);
        happInitEvnt.getBeneficiaries().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiary)) {
            return false;
        }
        return id != null && id.equals(((Beneficiary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beneficiary{" +
            "id=" + getId() +
            ", eid='" + getEid() + "'" +
            ", uid='" + getUid() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", fullNameEn='" + getFullNameEn() + "'" +
            ", grpNumber=" + getGrpNumber() +
            ", nationality=" + getNationality() +
            ", genderId=" + getGenderId() +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
