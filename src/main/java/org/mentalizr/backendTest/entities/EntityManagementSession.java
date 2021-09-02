package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

public class EntityManagementSession {

    private final TestContext testContext;

    public EntityManagementSession(TestContext testContext) {
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

    public void logout() throws TestEntityException {
        try {
            new LogoutService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            throw new TestEntityException(e.getMessage(), e);
        }
    }

}
