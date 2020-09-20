package com.revature.models;

public enum Type {

    LODGING("Lodging"),
    TRAVEL("Travel"),
    FOOD("Food"),
    OTHER("Other");

    private String typeName;

    // enum constructors are implicitly private
    Type(String name) {
        this.typeName = name;
    }

    public static Type getByName(String name) {

        for (Type type : Type.values()) {
            if (type.typeName.equals(name)) {
                return type;
            }
        }

        return OTHER;



    }

    @Override
    public String toString() {
        return typeName;
    }

}