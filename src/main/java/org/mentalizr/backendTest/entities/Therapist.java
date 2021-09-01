package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;
import org.mentalizr.client.restService.userAdmin.TherapistAddService;
import org.mentalizr.client.restService.userAdmin.TherapistDeleteService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;

public abstract class Therapist extends TestEntity {

    protected String id;
    protected String passwordHash;

    public Therapist(TestContext testContext) {
        super(testContext);
    }

    public abstract boolean isActive();

    public abstract String getUsername();

    public abstract String getTitle();

    public abstract String getFirstname();

    public abstract String getLastname();

    public abstract int getGender();

    public abstract String getEmail();

    public abstract String getPassword();

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public String getId() {
        return this.id;
    }

    public TherapistAddSO getTherapistAddSO() {
        TherapistAddSO therapistAddSO = new TherapistAddSO();
        therapistAddSO.setActive(isActive());
        therapistAddSO.setUsername(getUsername());
        therapistAddSO.setTitle(getTitle());
        therapistAddSO.setFirstname(getFirstname());
        therapistAddSO.setLastname(getLastname());
        therapistAddSO.setGender(getGender());
        therapistAddSO.setEmail(getEmail());
        therapistAddSO.setPassword(getPassword());
        return therapistAddSO;
    }

    public void create() throws TestEntityException {
        TherapistAddSO therapistAddSO = getTherapistAddSO();

        try {
            TherapistAddSO therapistAddSOReturn
                    = new TherapistAddService(therapistAddSO, this.testContext.getRestCallContext()).call();

            this.id = therapistAddSOReturn.getUuid();
            this.passwordHash = therapistAddSOReturn.getPasswordHash();

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void delete() throws TestEntityException {
        try {
            new TherapistDeleteService(getUsername(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }


}
