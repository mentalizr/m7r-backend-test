package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_LoginLogoutTest {

    private static TestContext testContext;

    @BeforeAll
    public static void setup() {
        System.out.println("### Setup ###");
        testContext = TestContext.getInstance();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("### Login ###");
        try {
            new LoginService(testContext.getUser(), testContext.getPassword(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(2)
    void status() {
        System.out.println("### Status ###");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(sessionStatusSO.isValid());
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(3)
    void logout() {
        System.out.println("### Logout ###");
        try {
            new LogoutService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(4)
    void statusAfterLogout() {
        System.out.println("### Status after Logout ###");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertFalse(sessionStatusSO.isValid());
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

}
