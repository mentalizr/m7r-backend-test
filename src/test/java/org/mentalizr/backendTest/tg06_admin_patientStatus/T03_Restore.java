package org.mentalizr.backendTest.tg06_admin_patientStatus;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusGetAllService;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusRestoreService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.backup.PatientStatusCollectionSO;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_Restore {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient1;

    private static final String contentId = "test_m1_sm1_s1";

    @BeforeAll
    public static void setup() throws TestEntityException, RestServiceConnectionException, RestServiceHttpException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        patient1 = new Patient01(program, therapist, testContext);
        patient1.create();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
        session.loginAsAdmin();

        patient1.deletePatientStatus();
        patient1.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void restore() throws RestServiceConnectionException, RestServiceHttpException {
        System.out.println("\n>>> delete >>>");

        PatientStatusSO patientStatusSO = new PatientStatusSO();
        patientStatusSO.setUserId(patient1.getUserId());
        patientStatusSO.setLastContentId(contentId);

        new PatientStatusRestoreService(patientStatusSO, testContext.getRestCallContext()).call();

        PatientStatusCollectionSO patientStatusCollectionSO =
                new PatientStatusGetAllService(testContext.getRestCallContext()).call();

        PatientStatusSO patientStatusSOReturn = getPatientStatus(patient1.getUserId(), patientStatusCollectionSO);
        assertEquals(contentId, patientStatusSOReturn.getLastContentId());

    }

    private PatientStatusSO getPatientStatus(String userId, PatientStatusCollectionSO patientStatusCollectionSO) {
        for (PatientStatusSO patientStatusSO : patientStatusCollectionSO.getCollection()) {
            if (patientStatusSO.getUserId().equals(userId)) return patientStatusSO;
        }
        throw new RuntimeException("No PatientStatus found for user id [" + userId + "].");
    }

}
