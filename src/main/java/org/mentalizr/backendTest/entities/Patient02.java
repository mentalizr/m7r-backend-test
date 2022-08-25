package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;

public class Patient02 extends Patient {

    public Patient02(TestContext testContext) {
        super(testContext);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Long getPolicyConsent() {
        return 0L;
    }

    @Override
    public String getUsername() {
        return "autotest_patient_02";
    }

    @Override
    public String getFirstname() {
        return "patient_02_firstname";
    }

    @Override
    public String getLastname() {
        return "patient_02_lastname";
    }

    @Override
    public int getGender() {
        return 1;
    }

    @Override
    public String getEmail() {
        return "patient02@example.org";
    }

    @Override
    public String getPassword() {
        return "topsecret";
    }

    @Override
    public boolean isSecondFA() {
        return false;
    }

    @Override
    public Long getEmailConfirmation() {
        return 0L;
    }

    @Override
    public String getEmailConfToken() {
        return null;
    }

    @Override
    public String getEmailConfCode() {
        return null;
    }

    @Override
    public boolean isRenewPasswordRequired() {
        return false;
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

}
