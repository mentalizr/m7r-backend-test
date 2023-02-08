package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginAccessKeyService;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

public class Session {

    private final TestContext testContext;

    public Session(TestContext testContext) {
        this.testContext = testContext;
    }

    public void loginAsAdmin() throws TestEntityException {
        try {
            new LoginService(
                    this.testContext.getUser(),
                    this.testContext.getPassword(),
                    this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void login(Patient patient) throws TestEntityException {
        loginAsUser(patient.getUsername(), patient.getPassword());
    }

    public void login(Therapist therapist) throws TestEntityException {
        loginAsUser(therapist.getUsername(), therapist.getPassword());
    }

    public void login(AccessKeyUser accessKeyUser) throws TestEntityException {
        loginAsAccessKey(accessKeyUser.getAccessKey());
    }

    public void loginAsUser(String user, String password) throws TestEntityException {
        try {
            new LoginService(
                    user,
                    password,
                    this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void loginAsAccessKey(String accessKey) throws TestEntityException {
        try {
            new LoginAccessKeyService(
                    accessKey,
                    this.testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void logout() throws TestEntityException {
        try {
            new LogoutService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

}
