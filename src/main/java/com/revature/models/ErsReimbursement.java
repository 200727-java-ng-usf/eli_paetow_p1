package com.revature.models;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Reimbursement POJO
 */
public class ErsReimbursement {

    private Integer id;
    private Double amount;
    private Timestamp submitted;
    private Timestamp resolved;
    private String description;
    private Integer authorId;
    private Integer resolverId;
    private Status reimbursementStatus;
    private Type reimbursementType;


    public ErsReimbursement() {
        super();
    }

    public ErsReimbursement(Double amount, Type reimbursementType) {
        this.amount = amount;
        this.reimbursementType = reimbursementType;
    }

    /**
     * Constructor
     * @param amount
     * @param reimbursementType
     * @param description
     */
    public ErsReimbursement(Double amount, Type reimbursementType, String description) {
        this(amount, reimbursementType);
        this.description = description;
        System.out.println("In constructor");
    }

    public ErsReimbursement(Double amount, Type reimbursementType, String description, Timestamp submitted) {
        this(amount, reimbursementType, description);
        this.submitted = submitted;
        System.out.println("In constructor");
    }

    public ErsReimbursement(Double amount, Type reimbursementType, String description, Timestamp submitted, Status reimbursementStatus) {
        this(amount, reimbursementType, description, submitted);
        this.reimbursementStatus = reimbursementStatus;
        System.out.println("In constructor");
    }


    public ErsReimbursement(Double amount, Type reimbursementType, String description, Integer authorId) {
        this(amount, reimbursementType, description);
        this.authorId = authorId;
    }

    // constructor for when resolve information comes in
    public ErsReimbursement(Integer resolverId, Timestamp resolved, Status reimbursementStatus) {
        this.resolverId = resolverId;
        this.resolved = resolved;
        this.reimbursementStatus = reimbursementStatus;
    }

    public ErsReimbursement(Integer resolverId, Timestamp resolved, String reimbursementStatusAsString) {
        this.resolverId = resolverId;
        this.resolved = resolved;
        this.reimbursementStatus = Status.getByName(reimbursementStatusAsString);
    }



    public ErsReimbursement(Double amount, Timestamp submitted, String description, Integer authorId, Status reimbursementStatus, Type reimbursementType) {
        this(amount, reimbursementType, description, authorId);
        this.submitted = submitted;
        this.reimbursementStatus = reimbursementStatus;
        this.authorId = authorId;
    }


    public ErsReimbursement(Double amount, Timestamp submitted, Timestamp resolved, String description, Integer authorId, Integer resolverId, Status reimbursementStatus, Type reimbursementType) {
        this(amount, submitted, description, authorId, reimbursementStatus, reimbursementType);
        this.resolved = resolved;
        this.resolverId = resolverId;
    }


    //full constructor
    public ErsReimbursement(Integer id, Double amount, Timestamp submitted, Timestamp resolved, String description, Integer authorId, Integer resolverId, Status reimbursementStatus, Type reimbursementType) {
        this(amount, submitted, resolved, description, authorId, resolverId, reimbursementStatus, reimbursementType);
        this.id = id;

    }

    //copy constructor
    public ErsReimbursement(ErsReimbursement copy) {
        this(copy.id, copy.amount, copy.submitted, copy.resolved, copy.description, copy.authorId, copy.resolverId, copy.reimbursementStatus, copy.reimbursementType);
    }


    //getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getResolverId() {
        return resolverId;
    }

    public void setResolverId(Integer resolverId) {
        this.resolverId = resolverId;
    }

    public Status getReimbursementStatus() {
        return reimbursementStatus;
    }

    public void setReimbursementStatus(Status reimbursementStatus) {
        this.reimbursementStatus = reimbursementStatus;
    }

    public Type getReimbursementType() {
        return reimbursementType;
    }

    public void setReimbursementType(Type reimbursementType) {
        this.reimbursementType = reimbursementType;
    }

   //overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsReimbursement that = (ErsReimbursement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(submitted, that.submitted) &&
                Objects.equals(resolved, that.resolved) &&
                Objects.equals(description, that.description) &&
                Objects.equals(authorId, that.authorId) &&
                Objects.equals(resolverId, that.resolverId) &&
                Objects.equals(reimbursementStatus, that.reimbursementStatus) &&
                Objects.equals(reimbursementType, that.reimbursementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, submitted, resolved, description, authorId, resolverId, reimbursementStatus, reimbursementType);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ",\n Amount: " + amount +
                ' ' + "\n";
    }

    public String toStringWithFormattingForResolved() {
        return "ID: " + id +
                ",\n Amount: " + amount +
                ",\n Submitted: " + submitted.toInstant().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                ",\n Resolved: " + resolved.toInstant().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME) +
                ",\n Description: '" + description + '\'' +
                ",\n Author ID: " + authorId +
                ",\n Resolver ID: " + resolverId +
                ",\n Status: " + reimbursementStatus +
                ",\n Type: " + reimbursementType +
                ' ' + "\n";
    }

    public String toStringOnlyUseThisForPending() {
        return "ID: " + id +
                ",\n Amount: " + amount +
                ",\n Submitted: " + submitted.toInstant().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME) +

                ",\n Description: '" + description + '\'' +
                ",\n AuthorId: " + authorId +

                ",\n Type: " + reimbursementType +
                ' ' + "\n";
    }
}