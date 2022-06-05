package com.example.android_database.throwable;

public class PeopleException extends Exception {

    public PeopleException(String message) {
        super(message);
    }

    public PeopleException(Throwable cause) {
        super(cause);
    }
}