package ae.gov.dubaipolice.happy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "attach_file", nullable = false)
    private byte[] attachFile;

    @NotNull
    @Column(name = "attach_file_content_type", nullable = false)
    private String attachFileContentType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "attachments", "happEvntTyp", "happInitTyp", "happTargt", "happEvntPlac", "employees", "beneficiaries" },
        allowSetters = true
    )
    private HappInitEvnt happInitEvnt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getAttachFile() {
        return this.attachFile;
    }

    public Attachment attachFile(byte[] attachFile) {
        this.setAttachFile(attachFile);
        return this;
    }

    public void setAttachFile(byte[] attachFile) {
        this.attachFile = attachFile;
    }

    public String getAttachFileContentType() {
        return this.attachFileContentType;
    }

    public Attachment attachFileContentType(String attachFileContentType) {
        this.attachFileContentType = attachFileContentType;
        return this;
    }

    public void setAttachFileContentType(String attachFileContentType) {
        this.attachFileContentType = attachFileContentType;
    }

    public HappInitEvnt getHappInitEvnt() {
        return this.happInitEvnt;
    }

    public void setHappInitEvnt(HappInitEvnt happInitEvnt) {
        this.happInitEvnt = happInitEvnt;
    }

    public Attachment happInitEvnt(HappInitEvnt happInitEvnt) {
        this.setHappInitEvnt(happInitEvnt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", attachFile='" + getAttachFile() + "'" +
            ", attachFileContentType='" + getAttachFileContentType() + "'" +
            "}";
    }
}
