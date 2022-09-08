package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;

public class Therapist01 extends Therapist {

    public Therapist01(TestContext testContext) {
        super(testContext);
    }

    @Override
    public TherapistAddSO getTherapistAddSO() {
        TherapistAddSO therapistAddSO = new TherapistAddSO();
        therapistAddSO.setActive(true);
        therapistAddSO.setRequirePolicyConsent(false);
        therapistAddSO.setUsername("autotest_therapist_01");
        therapistAddSO.setPassword("super_secret");
        therapistAddSO.setEmail("therapist01@example.org");
        therapistAddSO.setTitle("Dr. med.");
        therapistAddSO.setFirstname("therapist_01_firstname");
        therapistAddSO.setLastname("therapist_01_lastname");
        therapistAddSO.setGender(1);
        therapistAddSO.setRequire2FA(false);
        therapistAddSO.setRequireEmailConfirmation(false);
        therapistAddSO.setRequireRenewPassword(false);
        return therapistAddSO;
    }

}
