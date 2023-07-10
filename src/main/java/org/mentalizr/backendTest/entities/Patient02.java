package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;

public class Patient02 extends Patient {

    public Patient02(Program program, Therapist therapist, TestContext testContext) {
        super(program, therapist, testContext);
    }

    @Override
    public PatientAddSO getPatientAddSO() {
        PatientAddSO patientAddSO = new PatientAddSO();
        patientAddSO.setActive(true);
        patientAddSO.setRequirePolicyConsent(false);
        patientAddSO.setUsername("autotest_patient_02");
        patientAddSO.setPassword("topsecret");
        patientAddSO.setEmail("patient02@example.org");
        patientAddSO.setFirstname("patient_02_firstname");
        patientAddSO.setLastname("patient_02_lastname");
        patientAddSO.setGender(1);
        patientAddSO.setRequire2FA(false);
        patientAddSO.setRequireEmailConfirmation(false);
        patientAddSO.setRequireRenewPassword(false);
        patientAddSO.setProgramId(this.program.getProgramId());
        patientAddSO.setBlocking(true);
        patientAddSO.setTherapistId(this.therapist.getUserId());

        return patientAddSO;
    }

}
