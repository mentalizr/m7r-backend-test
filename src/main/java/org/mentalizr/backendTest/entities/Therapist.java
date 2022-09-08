package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.userAdmin.*;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.*;

import java.util.Optional;

public abstract class Therapist extends TestEntity {

    protected TherapistRestoreSO therapistRestoreSO;

    public Therapist(TestContext testContext) {
        super(testContext);
    }

    public abstract TherapistAddSO getTherapistAddSO();

    public String getUserId() {
        if (this.therapistRestoreSO == null) throw new IllegalStateException("Therapist not created yet.");
        return this.therapistRestoreSO.getUserId();
    }

    public String getUsername() {
        return this.getTherapistAddSO().getUsername();
    }

    public String getPassword() {
        return this.getTherapistAddSO().getPassword();
    }

    public TherapistRestoreSO getTherapistRestoreSO() {
        if (this.therapistRestoreSO == null) throw new IllegalStateException("Therapist not created yet.");
        return this.therapistRestoreSO;
    }

    public TherapistAddSO create() throws TestEntityException {
        TherapistAddSO therapistAddSO = getTherapistAddSO();

        try {
            TherapistAddSO therapistAddSOReturn
                    = new TherapistAddService(therapistAddSO, this.testContext.getRestCallContext()).call();
            this.therapistRestoreSO = new TherapistGetService(therapistAddSO.getUsername(), this.testContext.getRestCallContext()).call();
            return therapistAddSOReturn;
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void delete() throws TestEntityException {
        try {
            new TherapistDeleteService(
                    this.therapistRestoreSO.getUsername(),
                    testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public TherapistRestoreSO get() throws TestEntityException {
        try {
            return new TherapistGetService(
                    this.therapistRestoreSO.getUsername(),
                    testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public TherapistRestoreSO find() throws TestEntityException, TestEntityNotFoundException {
        TherapistRestoreCollectionSO therapistRestoreCollectionSO;
        try {
            therapistRestoreCollectionSO = new TherapistGetAllService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }

        String username = this.therapistRestoreSO.getUsername();
        Optional<TherapistRestoreSO> therapistRestoreSOOptional
                = therapistRestoreCollectionSO
                .getCollection()
                .stream()
                .filter(therapistRestoreSO -> therapistRestoreSO.getUsername().equals(username))
                .findAny();

        if (therapistRestoreSOOptional.isEmpty())
            throw new TestEntityNotFoundException("Therapist [" + username + "] not found.");

        return therapistRestoreSOOptional.get();
    }

}
