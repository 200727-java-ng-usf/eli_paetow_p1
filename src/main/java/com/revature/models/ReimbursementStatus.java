package com.revature.models;

public enum ReimbursementStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied");


    private String reimbursementStatus;

    // enum constructors are implicitly private
    ReimbursementStatus(String name) {
        this.reimbursementStatus = name;
    }

    public static ReimbursementStatus getByName(String name) {

        for (ReimbursementStatus role : ReimbursementStatus.values()) {
            if (role.reimbursementStatus.equals(name)) {
                return role;
            }
        }

        return PENDING;



    }

    @Override
    public String toString() {
        return reimbursementStatus;
    }
}
