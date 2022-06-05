package com.example.android_database.model;

import androidx.annotation.Nullable;

import com.example.android_database.throwable.PeopleException;

public class People {

    private int id;
    @Nullable private String firstName;
    @Nullable private String lastName;

    public People(int id, String firstName, String lastName) throws PeopleException {
        this.id = id;
        this.firstName = trimAndEmptyToNull(firstName);
        this.lastName = trimAndEmptyToNull(lastName);

        if (isEmptyPerson()) { throw new PeopleException("first name and last name can't all be null"); }
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) { sb.append(firstName); }
        if (firstName != null && lastName != null) { sb.append(' '); }
        if (lastName != null) { sb.append(lastName); }
        return sb.toString();
    }

    private boolean isEmptyPerson() { return firstName == null && lastName == null; }

    private String trimAndEmptyToNull(String string) {
        if (string != null) { return string.trim().isEmpty() ? null : string.trim(); }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ id: ").append(id).append(", first_name: ");
        if (firstName != null) { sb.append("\""); }
        sb.append(firstName);
        if (firstName != null) { sb.append("\""); }
        sb.append(", last_name: ");
        if (lastName != null) { sb.append("\""); }
        sb.append(lastName);
        if (lastName != null) { sb.append("\""); }
        sb.append(" }");
        return sb.toString();
    }
}
