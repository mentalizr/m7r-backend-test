package org.mentalizr.backendTest.tg04_formData;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.documents.FormData;
import org.mentalizr.backendTest.documents.FormDataExercise01;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restService.therapist.SubmitFeedbackService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.commons.Dates;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.therapist.feedbackSubmission.FeedbackSubmissionSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_Feedback {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;
    private static FormData formData;

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

        formData = new FormDataExercise01(testContext);
        formData.send(patient);
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
    void checkExercise() throws TestEntityException {
        System.out.println("\n>>> checkExercise >>>");

        FormDataSO formDataSO;
        try {
            formDataSO = new FormDataGetService(formData.getContentId(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        assertEquals(formData.getContentId(), formDataSO.getContentId());
        assertEquals(patient.getUserId(), formDataSO.getUserId());

        assertTrue(FormDataSOs.isExercise(formDataSO));
        ExerciseSO exerciseSOReturn = formDataSO.getExercise();
        assertTrue(exerciseSOReturn.isSent());

        assertEquals(1, formDataSO.getFormElementDataList().size());

        session.logout();
    }

    @Test
    @Order(2)
    void submitFeedback() throws TestEntityException {
        System.out.println("\n>>> submitFeedback >>>");

        session.login(therapist);

        FeedbackSubmissionSO feedbackSubmissionSO = new FeedbackSubmissionSO();
        feedbackSubmissionSO.setUserId(patient.getUserId());
        feedbackSubmissionSO.setContentId(formData.getContentId());
        feedbackSubmissionSO.setFeedback("Some feedback text.");

        try {
            new SubmitFeedbackService(feedbackSubmissionSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        session.logout();
    }

    @Test
    @Order(3)
    void loadFeedback() throws TestEntityException {
        System.out.println("\n>>> loadFeedback >>>");

        session.login(patient);

        FormDataSO formDataSO;
        try {
            formDataSO = new FormDataGetService(formData.getContentId(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        assertEquals(formData.getContentId(), formDataSO.getContentId());
        assertEquals(patient.getUserId(), formDataSO.getUserId());

        assertTrue(FormDataSOs.isExercise(formDataSO));
        ExerciseSO exerciseSOReturn = formDataSO.getExercise();
        assertTrue(exerciseSOReturn.isSent());

        assertEquals(1, formDataSO.getFormElementDataList().size());

        assertTrue(FormDataSOs.hasFeedback(formDataSO));
        FeedbackSO feedbackSO = formDataSO.getFeedback();
        assertEquals("Some feedback text.", feedbackSO.getText());
        assertTrue(Dates.isNotOlderThanOneMinute(feedbackSO.getCreatedTimestamp()));
        assertTrue(feedbackSO.isSeenByPatient());
    }

}
