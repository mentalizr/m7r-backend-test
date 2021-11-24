package org.mentalizr.backendTest.entities;

import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

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

    public boolean hasStatusCode() {
        return (getCause() instanceof RestServiceHttpException);
    }

    public int getStatusCode() {
        if (!hasStatusCode()) throw new IllegalStateException("Has no status code. Check before calling.");
        return ((RestServiceHttpException) getCause()).getStatusCode();
    }

}
