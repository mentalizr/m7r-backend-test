package org.mentalizr.backendTest.tg06_admin_patientStatus;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.admin.patientStatus.PatientStatusDeleteService;
import org.mentalizr.client.restService.patient.PatientStatusService;
import org.mentalizr.client.restService.patient.ProgramContentService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_Delete {

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

        patient1 = new Patient01(testContext);
        patient1.create(therapist.getId(), program.getProgramId());

        session.logout();

        session.loginAsPatient(patient1);
        new ProgramContentService(contentId, testContext.getRestCallContext()).call();
        session.logout();
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
    void delete() throws RestServiceConnectionException, RestServiceHttpException, TestEntityException {
        System.out.println("\n>>> delete >>>");

        session.loginAsPatient(patient1);
        PatientStatusSO patientStatusSO = new PatientStatusService(testContext.getRestCallContext()).call();
        assertTrue(Strings.isSpecified(patientStatusSO.getLastContentId()));
        assertEquals(contentId, patientStatusSO.getLastContentId());
        session.logout();

        session.loginAsAdmin();
        new PatientStatusDeleteService(patient1.getId(), testContext.getRestCallContext()).call();
        session.logout();

        session.loginAsPatient(patient1);
        patientStatusSO = new PatientStatusService(testContext.getRestCallContext()).call();
        assertFalse(Strings.isSpecified(patientStatusSO.getLastContentId()));
        session.logout();
    }

}
