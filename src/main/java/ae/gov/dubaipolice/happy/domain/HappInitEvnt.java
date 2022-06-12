package ae.gov.dubaipolice.happy.domain;

import ae.gov.dubaipolice.happy.domain.enumeration.InitOrEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A HappInitEvnt.
 */
@Entity
@Table(name = "happ_init_evnt")
public class HappInitEvnt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "init_event_name")
    private String initEventName;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(name = "total_target")
    private Integer totalTarget;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "init_or_event")
    private InitOrEvent initOrEvent;

    @Column(name = "add_date")
    private LocalDate addDate;

    @Column(name = "add_by")
    private Long addBy;

    @OneToMany(mappedBy = "happInitEvnt")
    @JsonIgnoreProperties(value = { "happInitEvnt" }, allowSetters = true)
    private Set<Attachment> attachments = new HashSet<>();

    @ManyToOne
    private HappEvntTyp happEvntTyp;

    @ManyToOne
    private HappInitTyp happInitTyp;

    @ManyToOne
    private HappTargt happTargt;

    @ManyToOne
    private HappEvntPlac happEvntPlac;

    @ManyToMany
    @JoinTable(
        name = "rel_happ_init_evnt__employee",
        joinColumns = @JoinColumn(name = "happ_init_evnt_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonIgnoreProperties(value = { "department", "happInitEvnts" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_happ_init_evn__benefici_ce",
        joinColumns = @JoinColumn(name = "happ_init_evnt_id"),
        inverseJoinColumns = @JoinColumn(name = "beneficiary_id")
    )
    @JsonIgnoreProperties(value = { "happInitEvnts" }, allowSetters = true)
    private Set<Beneficiary> beneficiaries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HappInitEvnt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitEventName() {
        return this.initEventName;
    }

    public HappInitEvnt initEventName(String initEventName) {
        this.setInitEventName(initEventName);
        return this;
    }

    public void setInitEventName(String initEventName) {
        this.initEventName = initEventName;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public HappInitEvnt dateFrom(LocalDate dateFrom) {
        this.setDateFrom(dateFrom);
        return this;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }

    public HappInitEvnt dateTo(LocalDate dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getTotalTarget() {
        return this.totalTarget;
    }

    public HappInitEvnt totalTarget(Integer totalTarget) {
        this.setTotalTarget(totalTarget);
        return this;
    }

    public void setTotalTarget(Integer totalTarget) {
        this.totalTarget = totalTarget;
    }

    public String getNotes() {
        return this.notes;
    }

    public HappInitEvnt notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InitOrEvent getInitOrEvent() {
        return this.initOrEvent;
    }

    public HappInitEvnt initOrEvent(InitOrEvent initOrEvent) {
        this.setInitOrEvent(initOrEvent);
        return this;
    }

    public void setInitOrEvent(InitOrEvent initOrEvent) {
        this.initOrEvent = initOrEvent;
    }

    public LocalDate getAddDate() {
        return this.addDate;
    }

    public HappInitEvnt addDate(LocalDate addDate) {
        this.setAddDate(addDate);
        return this;
    }

    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    public Long getAddBy() {
        return this.addBy;
    }

    public HappInitEvnt addBy(Long addBy) {
        this.setAddBy(addBy);
        return this;
    }

    public void setAddBy(Long addBy) {
        this.addBy = addBy;
    }

    public Set<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        if (this.attachments != null) {
            this.attachments.forEach(i -> i.setHappInitEvnt(null));
        }
        if (attachments != null) {
            attachments.forEach(i -> i.setHappInitEvnt(this));
        }
        this.attachments = attachments;
    }

    public HappInitEvnt attachments(Set<Attachment> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public HappInitEvnt addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setHappInitEvnt(this);
        return this;
    }

    public HappInitEvnt removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setHappInitEvnt(null);
        return this;
    }

    public HappEvntTyp getHappEvntTyp() {
        return this.happEvntTyp;
    }

    public void setHappEvntTyp(HappEvntTyp happEvntTyp) {
        this.happEvntTyp = happEvntTyp;
    }

    public HappInitEvnt happEvntTyp(HappEvntTyp happEvntTyp) {
        this.setHappEvntTyp(happEvntTyp);
        return this;
    }

    public HappInitTyp getHappInitTyp() {
        return this.happInitTyp;
    }

    public void setHappInitTyp(HappInitTyp happInitTyp) {
        this.happInitTyp = happInitTyp;
    }

    public HappInitEvnt happInitTyp(HappInitTyp happInitTyp) {
        this.setHappInitTyp(happInitTyp);
        return this;
    }

    public HappTargt getHappTargt() {
        return this.happTargt;
    }

    public void setHappTargt(HappTargt happTargt) {
        this.happTargt = happTargt;
    }

    public HappInitEvnt happTargt(HappTargt happTargt) {
        this.setHappTargt(happTargt);
        return this;
    }

    public HappEvntPlac getHappEvntPlac() {
        return this.happEvntPlac;
    }

    public void setHappEvntPlac(HappEvntPlac happEvntPlac) {
        this.happEvntPlac = happEvntPlac;
    }

    public HappInitEvnt happEvntPlac(HappEvntPlac happEvntPlac) {
        this.setHappEvntPlac(happEvntPlac);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public HappInitEvnt employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public HappInitEvnt addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getHappInitEvnts().add(this);
        return this;
    }

    public HappInitEvnt removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getHappInitEvnts().remove(this);
        return this;
    }

    public Set<Beneficiary> getBeneficiaries() {
        return this.beneficiaries;
    }

    public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public HappInitEvnt beneficiaries(Set<Beneficiary> beneficiaries) {
        this.setBeneficiaries(beneficiaries);
        return this;
    }

    public HappInitEvnt addBeneficiary(Beneficiary beneficiary) {
        this.beneficiaries.add(beneficiary);
        beneficiary.getHappInitEvnts().add(this);
        return this;
    }

    public HappInitEvnt removeBeneficiary(Beneficiary beneficiary) {
        this.beneficiaries.remove(beneficiary);
        beneficiary.getHappInitEvnts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HappInitEvnt)) {
            return false;
        }
        return id != null && id.equals(((HappInitEvnt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HappInitEvnt{" +
            "id=" + getId() +
            ", initEventName='" + getInitEventName() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", totalTarget=" + getTotalTarget() +
            ", notes='" + getNotes() + "'" +
            ", initOrEvent='" + getInitOrEvent() + "'" +
            ", addDate='" + getAddDate() + "'" +
            ", addBy=" + getAddBy() +
            "}";
    }
}
