package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;

public class Therapist01 extends Therapist {

    public Therapist01(TestContext testContext) {
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
        return "autotest_therapist_01";
    }

    @Override
    public String getTitle() {
        return "Dr. med.";
    }

    @Override
    public String getFirstname() {
        return "therapist_01_firstname";
    }

    @Override
    public String getLastname() {
        return "therapist_02_lastname";
    }

    @Override
    public int getGender() {
        return 0;
    }

    @Override
    public String getEmail() {
        return "thera01@example.org";
    }

    @Override
    public String getPassword() {
        return "super_secret";
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

}
