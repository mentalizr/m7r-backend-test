package org.mentalizr.backendTest.tg03_patient;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.ApplicationConfigService;
import org.mentalizr.client.restService.patient.PatientStatusService;
import org.mentalizr.client.restService.patient.ProgramContentService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSOX;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_PatientStatusTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    private static final String contentId = "test_m1_sm1_s1";

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

        patient = new Patient01(testContext);
        patient.create(therapist.getId(), program.getProgramId());

        session.logout();

        session.loginAsUser(patient.getUsername(), patient.getPassword());
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
    void fetchAnyContent() {
        System.out.println("\nn>>> fetch any content >>>");
        try {
            new ProgramContentService(contentId, testContext.getRestCallContext()).call();
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
            assertEquals(contentId, patientStatusSO.getLastContentId());
        } catch (RestServiceConnectionException | RestServiceHttpException e) {
            fail(e);
        }
    }

}
