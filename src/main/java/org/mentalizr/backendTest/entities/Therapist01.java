package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;

public class Therapist01 extends Therapist {

    public static final boolean active = true;
    public static final String username = "autotest_therapist_01";
    public static final String title = "Dr. med.";
    public static final String firstname = "therapist_01_firstname";
    public static final String lastname = "therapist_02_lastname";
    public static final int gender = 0;
    public static final String email = "thera01@example.org";
    public static final String password = "super_secret";
    public static String id;
    public static String passwordHash;

    public Therapist01(TestContext testContext) {
        super(testContext);
    }

    @Override
    public boolean isActive() {
        return true;
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

}
