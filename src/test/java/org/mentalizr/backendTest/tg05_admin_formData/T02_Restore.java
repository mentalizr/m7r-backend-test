package org.mentalizr.backendTest.tg05_admin_formData;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.documents.FormData;
import org.mentalizr.backendTest.documents.FormDataExercise01;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.admin.formData.FormDataRestoreService;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOX;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_Restore {

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
        session.loginAsAdmin();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
        session.loginAsAdmin();

        patient.cleanFormData();
        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void restore() throws TestEntityException {
        System.out.println("\n>>> restore >>>");

        FormData formData = new FormDataExercise01(testContext);
        FormDataSO formDataSO = formData.getFormDataSO(patient);

        try {
            new FormDataRestoreService(formDataSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        session.logout();
        session.login(patient);

        FormDataSO formDataSOReturn;
        try {
            formDataSOReturn = new FormDataGetService(formData.getContentId(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        System.out.println("Restored FormDataSO:\n" + FormDataSOX.toJsonWithFormatting(formDataSOReturn));

        assertNotNull(formDataSOReturn);

        String formDataSOJson = FormDataSOX.toJson(formDataSO);
        String formDataSOJsonReturn = FormDataSOX.toJson(formDataSOReturn);

        assertEquals(formDataSOJson, formDataSOJsonReturn);
    }

}
