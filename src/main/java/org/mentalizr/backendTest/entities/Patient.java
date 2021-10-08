package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.admin.FormDataCleanService;
import org.mentalizr.client.restService.userAdmin.*;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.*;

import java.util.Optional;

public abstract class Patient extends TestEntity {

    protected String id;
    protected String passwordHash;

    public Patient(TestContext testContext) {
        super(testContext);
    }

    public abstract boolean isActive();

    public abstract String getUsername();

    public abstract String getFirstname();

    public abstract String getLastname();

    public abstract int getGender();

    public abstract String getEmail();

    public abstract String getPassword();

    public abstract boolean isBlocking();

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public String getId() {
        return this.id;
    }

    public PatientAddSO getPatientAddSO(String therapistId, String programId) {
        PatientAddSO patientAddSO = new PatientAddSO();
        patientAddSO.setActive(isActive());
        patientAddSO.setUsername(getUsername());
        patientAddSO.setFirstname(getFirstname());
        patientAddSO.setLastname(getLastname());
        patientAddSO.setGender(getGender());
        patientAddSO.setEmail(getEmail());
        patientAddSO.setPassword(getPassword());
        patientAddSO.setTherapistId(therapistId);
        patientAddSO.setProgramId(programId);
        patientAddSO.setBlocking(isBlocking());
        return patientAddSO;
    }

    public PatientAddSO create(String therapistId, String programId) throws TestEntityException {
        PatientAddSO patientAddSO = getPatientAddSO(therapistId, programId);

        try {
            PatientAddSO patientAddSOReturn
                    = new PatientAddService(patientAddSO, this.testContext.getRestCallContext()).call();

            this.id = patientAddSOReturn.getUuid();
            this.passwordHash = patientAddSOReturn.getPasswordHash();

            return patientAddSOReturn;

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void cleanFormData() throws TestEntityException {
        try {
            new FormDataCleanService(this.id, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void delete() throws TestEntityException {
        try {
            new PatientDeleteService(getUsername(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public PatientRestoreSO get() throws TestEntityException {
        try {
            return new PatientGetService(getUsername(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public PatientRestoreSO find() throws TestEntityException, TestEntityNotFoundException {
        PatientRestoreCollectionSO patientRestoreCollectionSO;
        try {
            patientRestoreCollectionSO = new PatientGetAllService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }

        Optional<PatientRestoreSO> patientRestoreSOOptional
                = patientRestoreCollectionSO
                .getCollection()
                .stream()
                .filter(patientRestoreSO -> patientRestoreSO.getUsername().equals(getUsername()))
                .findAny();

        if (patientRestoreSOOptional.isEmpty())
            throw new TestEntityNotFoundException("Patient [" + getUsername() + "] not found.");

        return patientRestoreSOOptional.get();
    }

}
