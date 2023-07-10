package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restService.userAdmin.PatientGetService;
import org.mentalizr.client.restService.userAdmin.UserActivateService;
import org.mentalizr.client.restService.userAdmin.UserDeactivateService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T11_ActivateDeactivateCyclePatient {

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

    @Test
    @Order(2)
    void deactivate() {
        System.out.println("\n>>> deactivate");
        try {
            session.loginAsAdmin();
            new UserDeactivateService(patient.getUserId(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        } catch (TestEntityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void getPatientCheckFlag() {
        System.out.println("\n>>> get patient and check flag 'active'");

        try {
            PatientRestoreSO patientRestoreSO
                    = new PatientGetService(patient.getUsername(), testContext.getRestCallContext()).call();
            assertFalse(patientRestoreSO.isActive());
            session.logout();
        } catch (RestServiceHttpException | RestServiceConnectionException | TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void loginWhenDeactivated() {
        System.out.println("\n>>> login when deactivated >>>");

        RestServiceHttpException e = Assertions.assertThrows(RestServiceHttpException.class, () -> new LoginService(
                patient.getPatientRestoreSO().getUsername(),
                patient.getPassword(),
                testContext.getRestCallContext()
        ).call());

        assertEquals(401, e.getStatusCode());
    }

    @Test
    @Order(5)
    void activate() {
        System.out.println("\n>>> activate");
        try {
            session.loginAsAdmin();
            new UserActivateService(patient.getUserId(), testContext.getRestCallContext()).call();
            session.logout();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        } catch (TestEntityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    void loginWhenActivated() {
        System.out.println("\n>>> login when activated >>>");
        try {
            new LoginService(
                    patient.getPatientRestoreSO().getUsername(),
                    patient.getPassword(),
                    testContext.getRestCallContext()
            ).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
