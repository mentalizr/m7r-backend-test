package org.mentalizr.backendTest.tg05_admin_formData;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.documents.FormData;
import org.mentalizr.backendTest.documents.FormDataExercise01;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.admin.FormDataGetAllService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.backup.FormDataCollectionSO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_GetAll {

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

        patient = new Patient01(testContext);
        patient.create(therapist.getId(), program.getProgramId());

        session.logout();
        session.loginAsPatient(patient);

        formData = new FormDataExercise01(testContext);
        formData.send(patient);

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
    void getAll() {
        System.out.println("\n>>> getAll >>>");

        FormDataCollectionSO formDataCollectionSO;
        try {
            formDataCollectionSO = new FormDataGetAllService(patient.getId(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        assertEquals(1, formDataCollectionSO.getCollection().size());
    }

//    @Test
//    @Order(2)
//    void submitFeedback() throws TestEntityException {
//        System.out.println("\n>>> submitFeedback >>>");
//
//        session.loginAsTherapist(therapist);
//
//        FeedbackSubmissionSO feedbackSubmissionSO = new FeedbackSubmissionSO();
//        feedbackSubmissionSO.setUserId(patient.getId());
//        feedbackSubmissionSO.setContentId(formData.getContentId());
//        feedbackSubmissionSO.setFeedback("Some feedback text.");
//
//        try {
//            new SubmitFeedbackService(feedbackSubmissionSO, testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage(), e);
//        }
//
//        session.logout();
//    }
//
//    @Test
//    @Order(3)
//    void loadFeedback() throws TestEntityException {
//        System.out.println("\n>>> loadFeedback >>>");
//
//        session.loginAsPatient(patient);
//
//        FormDataSO formDataSO;
//        try {
//            formDataSO = new FormDataGetService(formData.getContentId(), testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage(), e);
//        }
//
//        assertEquals(formData.getContentId(), formDataSO.getContentId());
//        assertEquals(patient.getId(), formDataSO.getUserId());
//
//        assertTrue(FormDataSOs.isExercise(formDataSO));
//        ExerciseSO exerciseSOReturn = formDataSO.getExerciseSO();
//        assertTrue(exerciseSOReturn.isSent());
//
//        assertEquals(1, formDataSO.getFormElementDataList().size());
//
//        assertTrue(FormDataSOs.hasFeedback(formDataSO));
//        FeedbackSO feedbackSO = formDataSO.getFeedbackSO();
//        assertEquals("Some feedback text.", feedbackSO.getText());
//        assertTrue(Dates.isNotOlderThanOneMinute(feedbackSO.getCreatedTimestamp()));
//        assertFalse(feedbackSO.isSeenByPatient());
//    }

}
