package com.revature.models;


public enum Role {

    ADMIN("Admin"),
    FINANCE_MANAGER("FinManager"),
    EMPLOYEE("Employee"),
    INACTIVE("Inactive");

    private String roleName;

    // enum constructors are implicitly private
    Role(String name) {
        this.roleName = name;
    }

    public static Role getByName(String name) {

        for (Role role : Role.values()) {
            if (role.roleName.equals(name)) {
                return role;
            }
        }

        return FINANCE_MANAGER; // default is inactive



    }



    @Override
    public String toString() {
        return roleName;
    }

}