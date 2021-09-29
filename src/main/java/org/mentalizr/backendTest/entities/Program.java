package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.userAdmin.ProgramAddService;
import org.mentalizr.client.restService.userAdmin.ProgramDeleteService;
import org.mentalizr.client.restService.userAdmin.ProgramGetAllService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.ProgramCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;

public abstract class Program extends TestEntity {

    public Program(TestContext testContext) {
        super(testContext);
    }

    public abstract String getProgramId();

    public ProgramSO getProgramSO() {
        ProgramSO programSO = new ProgramSO();
        programSO.setProgramId(getProgramId());
        return programSO;
    }

    public void create() throws TestEntityException {
        ProgramSO programSO = getProgramSO();
        try {
            new ProgramAddService(programSO, this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void delete() throws TestEntityException {
        String programId = getProgramId();
        try {
            new ProgramDeleteService(programId, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public ProgramSO find() throws TestEntityException, TestEntityNotFoundException {
        ProgramCollectionSO programCollectionSO;
        try {
            programCollectionSO = new ProgramGetAllService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }

        for (ProgramSO programSO : programCollectionSO.getCollection()) {
            if (programSO.getProgramId().equals(getProgramId())) {
                return programSO;
            }
        }

        throw new TestEntityNotFoundException("Program [" + getProgramId() + "] not found.");
    }

}
