package org.mentalizr.backendTest.entities;

public class TestEntityNotFoundException extends Exception {

    public TestEntityNotFoundException() {
    }

    public TestEntityNotFoundException(String message) {
        super(message);
    }

    public TestEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestEntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public TestEntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
