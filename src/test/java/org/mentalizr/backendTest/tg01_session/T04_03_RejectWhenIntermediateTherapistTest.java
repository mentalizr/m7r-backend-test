package org.mentalizr.backendTest.tg01_session;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_03_RejectWhenIntermediateTherapistTest extends T04_00_RejectUnauthorizedAllMethodsBase {

    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        T04_00_RejectUnauthorizedAllMethodsBase.testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist02(testContext);
        therapist.create();

        patient = new Patient03(program, therapist, testContext);
        patient.create();

        session.logout();

        session.login(therapist);
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
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
            assertTrue(SessionStatusSOs.isIntermediate(sessionStatusSO));
            assertEquals("THERAPIST", sessionStatusSO.getUserRole());
            assertTrue(Strings.isSpecified(sessionStatusSO.getSessionId()));
            assertEquals("POLICY_CONSENT", sessionStatusSO.getRequire());
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    // find test cases in base class
}
