package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;

public class Patient01 extends Patient {

    public Patient01(TestContext testContext) {
        super(testContext);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getUsername() {
        return "autotest_patient_01";
    }

    @Override
    public String getFirstname() {
        return "patient_01_firstname";
    }

    @Override
    public String getLastname() {
        return "patient_01_lastname";
    }

    @Override
    public int getGender() {
        return 1;
    }

    @Override
    public String getEmail() {
        return "patient01@example.org";
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
