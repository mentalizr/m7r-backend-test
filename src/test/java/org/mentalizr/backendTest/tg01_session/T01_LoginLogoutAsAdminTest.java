package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_LoginLogoutAsAdminTest {

    private static TestContext testContext;

    @BeforeAll
    public static void setup() {
        System.out.println("\n>>> setup >>>");
        testContext = TestContext.getInstance();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("\n>>> login >>>");
        try {
            new LoginService(testContext.getUser(), testContext.getPassword(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void status() {
        System.out.println("\n>>> session status >>>");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(SessionStatusSOs.isValid(sessionStatusSO));
            assertEquals("ADMIN", sessionStatusSO.getUserRole());
            assertTrue(Strings.isSpecified(sessionStatusSO.getSessionId()));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void logout() {
        System.out.println("\n>>> logout >>>");
        try {
            new LogoutService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void statusAfterLogout() {
        System.out.println("\n>>> status after Logout >>>");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(SessionStatusSOs.isInvalid(sessionStatusSO));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

}
