package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.generic.ChangePasswordService;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;
import org.mentalizr.serviceObjects.frontend.application.ChangePasswordSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T12_ChangePasswordPatient {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

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

        patient = new Patient01(program, therapist, testContext);

        patient.create();

        session.logout();
        session.login(patient);
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.loginAsAdmin();

        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void changePassword() {
        System.out.println("\n>>> changePassword >>>");
        ChangePasswordSO changePasswordSO = new ChangePasswordSO();
        changePasswordSO.setPassword("newPassword");
        try {
            new ChangePasswordService(changePasswordSO, testContext.getRestCallContext()).call();
            session.logout();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        } catch (TestEntityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void reLoginWithNewPassword() {
        System.out.println("\n>>> re-login with new password");
        try {
            new LoginService(
                    patient.getUsername(),
                    "newPassword",
                    testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void status() {
        System.out.println("\n>>> session status >>>");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(SessionStatusSOs.isValid(sessionStatusSO));
            assertEquals("LOGIN_PATIENT", sessionStatusSO.getUserRole());
            assertTrue(Strings.isSpecified(sessionStatusSO.getSessionId()));
            session.logout();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        } catch (TestEntityException e) {
            throw new RuntimeException(e);
        }
    }

}
