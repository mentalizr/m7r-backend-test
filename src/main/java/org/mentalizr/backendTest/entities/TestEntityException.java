package org.mentalizr.backendTest.entities;

public class TestEntityException extends Exception {

    public TestEntityException() {
    }

    public TestEntityException(String message) {
        super(message);
    }

    public TestEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestEntityException(Throwable cause) {
        super(cause);
    }

    public TestEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
