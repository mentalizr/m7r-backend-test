package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.LoginAccessKeyService;
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
public class T03_LoginLogoutAsAccessKeyUserTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static AccessKeyUser accessKeyUser;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        accessKeyUser = new AccessKeyUser01(program, therapist, testContext);
        accessKeyUser.create();

        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.loginAsAdmin();

        accessKeyUser.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("\n>>> login >>>");
        try {
            new LoginAccessKeyService(
                    accessKeyUser.getAccessKey(),
                    testContext.getRestCallContext()
            ).call();
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
            assertTrue(SessionStatusSOs.isIntermediate(sessionStatusSO));
            assertEquals("POLICY_CONSENT", sessionStatusSO.getRequire());
            assertEquals("ANONYMOUS_PATIENT", sessionStatusSO.getUserRole());
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
            assertFalse(SessionStatusSOs.isValid(sessionStatusSO));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }
}
