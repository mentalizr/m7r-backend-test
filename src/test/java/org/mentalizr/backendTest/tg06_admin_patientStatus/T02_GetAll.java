package org.mentalizr.backendTest.tg06_admin_patientStatus;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusGetAllService;
import org.mentalizr.client.restService.patient.ProgramContentService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.backup.PatientStatusCollectionSO;
import org.mentalizr.serviceObjects.backup.PatientStatusCollectionSOX;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_GetAll {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient1;
    private static Patient patient2;

    private static final String contentIdPatient1 = "test_m1_sm1_s1";
    private static final String contentIdPatient2 = "test_m1_sm1_s2";

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

        patient1 = new Patient01(testContext);
        patient1.create(therapist.getId(), program.getProgramId());

        patient2 = new Patient02(testContext);
        patient2.create(therapist.getId(), program.getProgramId());

        session.logout();

        session.loginAsPatient(patient1);
        new ProgramContentService(contentIdPatient1, testContext.getRestCallContext()).call();
        session.logout();

        session.loginAsPatient(patient2);
        new ProgramContentService(contentIdPatient2, testContext.getRestCallContext()).call();
        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
        session.loginAsAdmin();

        patient1.deletePatientStatus();
        patient1.delete();
        patient2.deletePatientStatus();
        patient2.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void getAll() throws TestEntityException {
        System.out.println("\n>>> getAll >>>");

        session.loginAsAdmin();

        PatientStatusCollectionSO patientStatusCollectionSO;
        try {
            patientStatusCollectionSO = new PatientStatusGetAllService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        System.out.println(PatientStatusCollectionSOX.toJson(patientStatusCollectionSO));
        assertTrue(patientStatusCollectionSO.getCollection().size() >= 2);

        PatientStatusSO patientStatusSO = getPatientStatus(patient1.getId(), patientStatusCollectionSO);
        assertEquals(patientStatusSO.getLastContentId(), contentIdPatient1);

        patientStatusSO = getPatientStatus(patient2.getId(), patientStatusCollectionSO);
        assertEquals(patientStatusSO.getLastContentId(), contentIdPatient2);
    }

    private PatientStatusSO getPatientStatus(String userId, PatientStatusCollectionSO patientStatusCollectionSO) {
        for (PatientStatusSO patientStatusSO : patientStatusCollectionSO.getCollection()) {
            if (patientStatusSO.getUserId().equals(userId)) return patientStatusSO;
        }
        throw new RuntimeException("No PatientStatus found for user id [" + userId + "].");
    }

}
