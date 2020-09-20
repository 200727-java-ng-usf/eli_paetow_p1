package com.revature.models;


public enum Status {

    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied");

    private String statusName;

    // enum constructors are implicitly private
    Status(String name) {
        this.statusName = name;
    }

    public static Status getByName(String name) {

        for (Status status : Status.values()) {
            if (status.statusName.equals(name)) {
                return status;
            }
        }

        return PENDING;



    }

    @Override
    public String toString() {
        return statusName;
    }
}