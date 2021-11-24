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
    public boolean isBlocking() {
        return true;
    }

}
