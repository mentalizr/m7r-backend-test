package org.mentalizr.backendTest.tg03_patient;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.PatientStatusService;
import org.mentalizr.client.restService.patient.ProgramContentService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_InfoTextLastVisitedTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    private static final String contentIdStep = "test_m1_sm1_s1";
    private static final String contentIdInfoText = "test__info_1";

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

        session.logout();
        session.loginAsAdmin();

        patient.deletePatientStatus();
        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void patientStatusOnFirstLogin() {
        System.out.println("\nn>>> patient status on first login >>>");
        try {
            PatientStatusSO patientStatusSO = new PatientStatusService(testContext.getRestCallContext()).call();
            assertFalse(Strings.isSpecified(patientStatusSO.getLastContentId()));
        } catch (RestServiceConnectionException | RestServiceHttpException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void fetchStepContent() {
        System.out.println("\nn>>> fetch step content >>>");
        try {
            new ProgramContentService(contentIdStep, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void patientStatusAfterSystemInteraction() {
        System.out.println("\nn>>> patient status after system interaction >>>");
        try {
            PatientStatusSO patientStatusSO = new PatientStatusService(testContext.getRestCallContext()).call();
            assertTrue(Strings.isSpecified(patientStatusSO.getLastContentId()));
            assertEquals(contentIdStep, patientStatusSO.getLastContentId());
        } catch (RestServiceConnectionException | RestServiceHttpException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void fetchInfoTextContent() {
        System.out.println("\nn>>> fetch info text content >>>");
        try {
            new ProgramContentService(contentIdInfoText, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(5)
    void patientStatusAfterFetchOfInfoTextContent() {
        System.out.println("\nn>>> patient status after fetch of info text content >>>");
        try {
            PatientStatusSO patientStatusSO = new PatientStatusService(testContext.getRestCallContext()).call();
            assertTrue(Strings.isSpecified(patientStatusSO.getLastContentId()));
            assertEquals(contentIdStep, patientStatusSO.getLastContentId());
        } catch (RestServiceConnectionException | RestServiceHttpException e) {
            fail(e);
        }
    }

}
