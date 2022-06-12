package ae.gov.dubaipolice.happy.service.criteria;

import ae.gov.dubaipolice.happy.domain.enumeration.InitOrEvent;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ae.gov.dubaipolice.happy.domain.HappInitEvnt} entity. This class is used
 * in {@link ae.gov.dubaipolice.happy.web.rest.HappInitEvntResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /happ-init-evnts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class HappInitEvntCriteria implements Serializable, Criteria {

    /**
     * Class for filtering InitOrEvent
     */
    public static class InitOrEventFilter extends Filter<InitOrEvent> {

        public InitOrEventFilter() {}

        public InitOrEventFilter(InitOrEventFilter filter) {
            super(filter);
        }

        @Override
        public InitOrEventFilter copy() {
            return new InitOrEventFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter initEventName;

    private LocalDateFilter dateFrom;

    private LocalDateFilter dateTo;

    private IntegerFilter totalTarget;

    private StringFilter notes;

    private InitOrEventFilter initOrEvent;

    private LocalDateFilter addDate;

    private LongFilter addBy;

    private LongFilter attachmentId;

    private LongFilter happEvntTypId;

    private LongFilter happInitTypId;

    private LongFilter happTargtId;

    private LongFilter happEvntPlacId;

    private LongFilter employeeId;

    private LongFilter beneficiaryId;

    private Boolean distinct;

    public HappInitEvntCriteria() {}

    public HappInitEvntCriteria(HappInitEvntCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.initEventName = other.initEventName == null ? null : other.initEventName.copy();
        this.dateFrom = other.dateFrom == null ? null : other.dateFrom.copy();
        this.dateTo = other.dateTo == null ? null : other.dateTo.copy();
        this.totalTarget = other.totalTarget == null ? null : other.totalTarget.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.initOrEvent = other.initOrEvent == null ? null : other.initOrEvent.copy();
        this.addDate = other.addDate == null ? null : other.addDate.copy();
        this.addBy = other.addBy == null ? null : other.addBy.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.happEvntTypId = other.happEvntTypId == null ? null : other.happEvntTypId.copy();
        this.happInitTypId = other.happInitTypId == null ? null : other.happInitTypId.copy();
        this.happTargtId = other.happTargtId == null ? null : other.happTargtId.copy();
        this.happEvntPlacId = other.happEvntPlacId == null ? null : other.happEvntPlacId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.beneficiaryId = other.beneficiaryId == null ? null : other.beneficiaryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HappInitEvntCriteria copy() {
        return new HappInitEvntCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInitEventName() {
        return initEventName;
    }

    public StringFilter initEventName() {
        if (initEventName == null) {
            initEventName = new StringFilter();
        }
        return initEventName;
    }

    public void setInitEventName(StringFilter initEventName) {
        this.initEventName = initEventName;
    }

    public LocalDateFilter getDateFrom() {
        return dateFrom;
    }

    public LocalDateFilter dateFrom() {
        if (dateFrom == null) {
            dateFrom = new LocalDateFilter();
        }
        return dateFrom;
    }

    public void setDateFrom(LocalDateFilter dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateFilter getDateTo() {
        return dateTo;
    }

    public LocalDateFilter dateTo() {
        if (dateTo == null) {
            dateTo = new LocalDateFilter();
        }
        return dateTo;
    }

    public void setDateTo(LocalDateFilter dateTo) {
        this.dateTo = dateTo;
    }

    public IntegerFilter getTotalTarget() {
        return totalTarget;
    }

    public IntegerFilter totalTarget() {
        if (totalTarget == null) {
            totalTarget = new IntegerFilter();
        }
        return totalTarget;
    }

    public void setTotalTarget(IntegerFilter totalTarget) {
        this.totalTarget = totalTarget;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public InitOrEventFilter getInitOrEvent() {
        return initOrEvent;
    }

    public InitOrEventFilter initOrEvent() {
        if (initOrEvent == null) {
            initOrEvent = new InitOrEventFilter();
        }
        return initOrEvent;
    }

    public void setInitOrEvent(InitOrEventFilter initOrEvent) {
        this.initOrEvent = initOrEvent;
    }

    public LocalDateFilter getAddDate() {
        return addDate;
    }

    public LocalDateFilter addDate() {
        if (addDate == null) {
            addDate = new LocalDateFilter();
        }
        return addDate;
    }

    public void setAddDate(LocalDateFilter addDate) {
        this.addDate = addDate;
    }

    public LongFilter getAddBy() {
        return addBy;
    }

    public LongFilter addBy() {
        if (addBy == null) {
            addBy = new LongFilter();
        }
        return addBy;
    }

    public void setAddBy(LongFilter addBy) {
        this.addBy = addBy;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public LongFilter attachmentId() {
        if (attachmentId == null) {
            attachmentId = new LongFilter();
        }
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getHappEvntTypId() {
        return happEvntTypId;
    }

    public LongFilter happEvntTypId() {
        if (happEvntTypId == null) {
            happEvntTypId = new LongFilter();
        }
        return happEvntTypId;
    }

    public void setHappEvntTypId(LongFilter happEvntTypId) {
        this.happEvntTypId = happEvntTypId;
    }

    public LongFilter getHappInitTypId() {
        return happInitTypId;
    }

    public LongFilter happInitTypId() {
        if (happInitTypId == null) {
            happInitTypId = new LongFilter();
        }
        return happInitTypId;
    }

    public void setHappInitTypId(LongFilter happInitTypId) {
        this.happInitTypId = happInitTypId;
    }

    public LongFilter getHappTargtId() {
        return happTargtId;
    }

    public LongFilter happTargtId() {
        if (happTargtId == null) {
            happTargtId = new LongFilter();
        }
        return happTargtId;
    }

    public void setHappTargtId(LongFilter happTargtId) {
        this.happTargtId = happTargtId;
    }

    public LongFilter getHappEvntPlacId() {
        return happEvntPlacId;
    }

    public LongFilter happEvntPlacId() {
        if (happEvntPlacId == null) {
            happEvntPlacId = new LongFilter();
        }
        return happEvntPlacId;
    }

    public void setHappEvntPlacId(LongFilter happEvntPlacId) {
        this.happEvntPlacId = happEvntPlacId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getBeneficiaryId() {
        return beneficiaryId;
    }

    public LongFilter beneficiaryId() {
        if (beneficiaryId == null) {
            beneficiaryId = new LongFilter();
        }
        return beneficiaryId;
    }

    public void setBeneficiaryId(LongFilter beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HappInitEvntCriteria that = (HappInitEvntCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(initEventName, that.initEventName) &&
            Objects.equals(dateFrom, that.dateFrom) &&
            Objects.equals(dateTo, that.dateTo) &&
            Objects.equals(totalTarget, that.totalTarget) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(initOrEvent, that.initOrEvent) &&
            Objects.equals(addDate, that.addDate) &&
            Objects.equals(addBy, that.addBy) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(happEvntTypId, that.happEvntTypId) &&
            Objects.equals(happInitTypId, that.happInitTypId) &&
            Objects.equals(happTargtId, that.happTargtId) &&
            Objects.equals(happEvntPlacId, that.happEvntPlacId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(beneficiaryId, that.beneficiaryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            initEventName,
            dateFrom,
            dateTo,
            totalTarget,
            notes,
            initOrEvent,
            addDate,
            addBy,
            attachmentId,
            happEvntTypId,
            happInitTypId,
            happTargtId,
            happEvntPlacId,
            employeeId,
            beneficiaryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HappInitEvntCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (initEventName != null ? "initEventName=" + initEventName + ", " : "") +
            (dateFrom != null ? "dateFrom=" + dateFrom + ", " : "") +
            (dateTo != null ? "dateTo=" + dateTo + ", " : "") +
            (totalTarget != null ? "totalTarget=" + totalTarget + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (initOrEvent != null ? "initOrEvent=" + initOrEvent + ", " : "") +
            (addDate != null ? "addDate=" + addDate + ", " : "") +
            (addBy != null ? "addBy=" + addBy + ", " : "") +
            (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
            (happEvntTypId != null ? "happEvntTypId=" + happEvntTypId + ", " : "") +
            (happInitTypId != null ? "happInitTypId=" + happInitTypId + ", " : "") +
            (happTargtId != null ? "happTargtId=" + happTargtId + ", " : "") +
            (happEvntPlacId != null ? "happEvntPlacId=" + happEvntPlacId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (beneficiaryId != null ? "beneficiaryId=" + beneficiaryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
