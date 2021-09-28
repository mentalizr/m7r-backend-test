package org.mentalizr.backendTest.tg04_formData;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.commons.Dates;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restService.patient.FormDataSaveService;
import org.mentalizr.client.restService.patient.FormDataSendService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class T03_Exercise {

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

        patient.cleanFormData();
        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    void writeRead() {

        System.out.println("\n>>> writeRead >>>");

        String contentId = "test_m1_sm1_someExercisePage";

        // create exercise initially ...

        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(patient.getId());
        formDataSO.setContentId(contentId);
        formDataSO.setEditable(false);

        ExerciseSO exerciseSO = new ExerciseSO();
        exerciseSO.setSent(false);
        exerciseSO.setLastModifiedTimestamp("");
        exerciseSO.setSeenByTherapist(false);
        exerciseSO.setSeenByTherapistTimestamp("");
        formDataSO.setExerciseSO(exerciseSO);

        List<FormElementDataSO> formElementDataSOList = new ArrayList<>();

        FormElementDataSO formElementDataSO = new FormElementDataSO();
        formElementDataSO.setFormElementId("id1");
        formElementDataSO.setFormElementType("input");
        formElementDataSO.setFormElementValue("some dummy text");
        formElementDataSOList.add(formElementDataSO);

        formElementDataSO = new FormElementDataSO();
        formElementDataSO.setFormElementId("id2");
        formElementDataSO.setFormElementType("input");
        formElementDataSO.setFormElementValue("");
        formElementDataSOList.add(formElementDataSO);

        formDataSO.setFormElementDataList(formElementDataSOList);

        try {
            new FormDataSendService(formDataSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        // reload exercise and check ...

        FormDataSO formDataSOReturn;
        try {
            formDataSOReturn = new FormDataGetService(contentId, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        assertEquals(contentId, formDataSOReturn.getContentId());
        assertEquals(patient.getId(), formDataSOReturn.getUserId());
        assertFalse(formDataSOReturn.isEditable());

        assertTrue(FormDataSOs.isExercise(formDataSOReturn));
        ExerciseSO exerciseSOReturn = formDataSOReturn.getExerciseSO();
        assertTrue(exerciseSOReturn.isSent());
        String lastModifiedTimestamp = exerciseSOReturn.getLastModifiedTimestamp();
        assertTrue(Dates.isNotOlderThanOneMinute(lastModifiedTimestamp));
        assertFalse(exerciseSOReturn.isSeenByTherapist());
        assertEquals("1970-01-01T00:00:00Z", exerciseSOReturn.getSeenByTherapistTimestamp());

        assertEquals(2, formDataSOReturn.getFormElementDataList().size());

        FormElementDataSO formElementDataSOReturn = formDataSOReturn.getFormElementDataList().get(0);
        assertEquals("id1", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("some dummy text", formElementDataSOReturn.getFormElementValue());

        formElementDataSOReturn = formDataSOReturn.getFormElementDataList().get(1);
        assertEquals("id2", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("", formElementDataSOReturn.getFormElementValue());

    }

}
