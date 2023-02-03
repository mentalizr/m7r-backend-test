package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.accessKey.AccessKeyCreateService;
import org.mentalizr.client.restService.accessKey.AccessKeyDeleteService;
import org.mentalizr.client.restService.admin.formData.FormDataCleanService;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusDeleteService;
import org.mentalizr.client.restService.userAdmin.PatientAddService;
import org.mentalizr.client.restService.userAdmin.PatientDeleteService;
import org.mentalizr.client.restService.userAdmin.PatientGetAllService;
import org.mentalizr.client.restService.userAdmin.PatientGetService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.*;

import java.util.Optional;

public abstract class AccessKeyUser extends TestEntity {

    protected AccessKeyRestoreSO accessKeyRestoreSO;
    protected Program program;
    protected Therapist therapist;

    public AccessKeyUser(Program program, Therapist therapist, TestContext testContext) {
        super(testContext);
        this.program = program;
        this.therapist = therapist;
    }

    public abstract AccessKeyCreateSO getAccessKeyCreateSO();

    public String getUserId() {
        if (this.accessKeyRestoreSO == null) throw new IllegalStateException("Patient not created yet.");
        return this.accessKeyRestoreSO.getUserId();
    }

    public String getAccessKey() {
        return this.accessKeyRestoreSO.getAccessKey();
    }

    public AccessKeyRestoreSO getAccessKeyRestoreSO() {
        if (this.accessKeyRestoreSO == null)
            throw new IllegalStateException(AccessKeyUser.class.getSimpleName() + " not created yet.");
        return this.accessKeyRestoreSO;
    }

    public void create() throws TestEntityException {
        AccessKeyCreateSO accessKeyCreateSO = getAccessKeyCreateSO();

        try {

            AccessKeyCollectionSO accessKeyCollectionSO
                    = new AccessKeyCreateService(accessKeyCreateSO, this.testContext.getRestCallContext()).call();
            this.accessKeyRestoreSO = accessKeyCollectionSO.getCollection().get(0);

//            PatientAddSO patientAddSOReturn
//                    = new PatientAddService(patientAddSO, this.testContext.getRestCallContext()).call();
//            this.accessKeyRestoreSO
//                    = new PatientGetService(patientAddSO.getUsername(), this.testContext.getRestCallContext()).call();
//            return accessKeyCollectionSO;
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
        AccessKeyDeleteSO accessKeyDeleteSO = new AccessKeyDeleteSO();
        accessKeyDeleteSO.setAccessKey(getAccessKey());
        try {
            new AccessKeyDeleteService(
                    accessKeyDeleteSO,
                    this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

//    public PatientRestoreSO get() throws TestEntityException {
//        try {
//            return new PatientGetService(
//                    getUsername(),
//                    this.testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            throw new TestEntityException(e.getMessage(), e);
//        }
//    }

//    public PatientRestoreSO find() throws TestEntityException, TestEntityNotFoundException {
//        PatientRestoreCollectionSO patientRestoreCollectionSO;
//        try {
//            patientRestoreCollectionSO = new PatientGetAllService(testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            throw new TestEntityException(e.getMessage(), e);
//        }
//
//        Optional<PatientRestoreSO> patientRestoreSOOptional
//                = patientRestoreCollectionSO
//                .getCollection()
//                .stream()
//                .filter(patientRestoreSO -> patientRestoreSO.getUsername().equals(this.accessKeyRestoreSO.getUsername()))
//                .findAny();
//
//        if (patientRestoreSOOptional.isEmpty())
//            throw new TestEntityNotFoundException("Patient [" + this.accessKeyRestoreSO.getUsername() + "] not found.");
//
//        return patientRestoreSOOptional.get();
//    }

}
