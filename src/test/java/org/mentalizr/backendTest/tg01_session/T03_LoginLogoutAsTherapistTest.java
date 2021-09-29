package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_LoginLogoutAsTherapistTest {

    private static TestContext testContext;
    private static Session session;

    private static Therapist therapist;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        therapist = new Therapist01(testContext);
        therapist.create();

        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.loginAsAdmin();

        therapist.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("\n>>> login >>>");
        try {
            new LoginService(therapist.getUsername(), therapist.getPassword(), testContext.getRestCallContext()).call();
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
            assertTrue(sessionStatusSO.isValid());
            assertEquals("THERAPIST", sessionStatusSO.getUserRole());
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
            assertFalse(sessionStatusSO.isValid());
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }
}
