package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.admin.formData.FormDataCleanService;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusDeleteService;
import org.mentalizr.client.restService.userAdmin.PatientAddService;
import org.mentalizr.client.restService.userAdmin.PatientDeleteService;
import org.mentalizr.client.restService.userAdmin.PatientGetAllService;
import org.mentalizr.client.restService.userAdmin.PatientGetService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import java.util.Optional;

public abstract class Patient extends TestEntity {

    protected PatientRestoreSO patientRestoreSO;
    protected Program program;
    protected Therapist therapist;

    public Patient(Program program, Therapist therapist, TestContext testContext) {
        super(testContext);
        this.program = program;
        this.therapist = therapist;
    }

    public abstract PatientAddSO getPatientAddSO();

    public String getUserId() {
        if (this.patientRestoreSO == null) throw new IllegalStateException("Patient not created yet.");
        return this.patientRestoreSO.getUserId();
    }

    public String getUsername() {
        return this.getPatientAddSO().getUsername();
    }

    public String getPassword() {
        return this.getPatientAddSO().getPassword();
    }

    public PatientRestoreSO getPatientRestoreSO() {
        if (this.patientRestoreSO == null) throw new IllegalStateException("Patient not created yet.");
        return this.patientRestoreSO;
    }

    public PatientAddSO create() throws TestEntityException {
        PatientAddSO patientAddSO = getPatientAddSO();

        try {
            PatientAddSO patientAddSOReturn
                    = new PatientAddService(patientAddSO, this.testContext.getRestCallContext()).call();
            this.patientRestoreSO
                    = new PatientGetService(patientAddSO.getUsername(), this.testContext.getRestCallContext()).call();
            return patientAddSOReturn;
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void cleanFormData() throws TestEntityException {
        try {
            new FormDataCleanService(
                    getUserId(),
                    this.testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void deletePatientStatus() throws TestEntityException {
        try {
            new PatientStatusDeleteService(
                    getUserId(),
                    testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void delete() throws TestEntityException {
        try {
            new PatientDeleteService(
                    getUsername(),
                    this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public PatientRestoreSO get() throws TestEntityException {
        try {
            return new PatientGetService(
                    getUsername(),
                    this.testContext.getRestCallContext()).call();
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
                .filter(patientRestoreSO -> patientRestoreSO.getUsername().equals(this.patientRestoreSO.getUsername()))
                .findAny();

        if (patientRestoreSOOptional.isEmpty())
            throw new TestEntityNotFoundException("Patient [" + this.patientRestoreSO.getUsername() + "] not found.");

        return patientRestoreSOOptional.get();
    }

}
