package org.mentalizr.backendTest.tg04_formData;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restService.patient.FormDataSaveService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class T02_ProgramGenericScope {

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

        String contentId = "test_program-generic";

        // create form data initially ...

        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(patient.getId());
        formDataSO.setContentId(contentId);
        formDataSO.setEditable(false);

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
            new FormDataSaveService(formDataSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        // reload form data and check ...

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
        assertEquals(2, formDataSOReturn.getFormElementDataList().size());

        FormElementDataSO formElementDataSOReturn = formDataSOReturn.getFormElementDataList().get(0);
        assertEquals("id1", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("some dummy text", formElementDataSOReturn.getFormElementValue());

        formElementDataSOReturn = formDataSOReturn.getFormElementDataList().get(1);
        assertEquals("id2", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("", formElementDataSOReturn.getFormElementValue());

        // update and add other element then save again ...

        formDataSO = new FormDataSO();
        formDataSO.setUserId(patient.getId());
        formDataSO.setContentId(contentId);
        formDataSO.setEditable(false);

        formElementDataSOList = new ArrayList<>();

        formElementDataSO = new FormElementDataSO();
        formElementDataSO.setFormElementId("id2");
        formElementDataSO.setFormElementType("input");
        formElementDataSO.setFormElementValue("some other dummy text as update");
        formElementDataSOList.add(formElementDataSO);

        formElementDataSO = new FormElementDataSO();
        formElementDataSO.setFormElementId("id3");
        formElementDataSO.setFormElementType("input");
        formElementDataSO.setFormElementValue("lorem ipsum");
        formElementDataSOList.add(formElementDataSO);

        formDataSO.setFormElementDataList(formElementDataSOList);

        try {
            new FormDataSaveService(formDataSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        // reload and check again ...

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
        assertEquals(3, formDataSOReturn.getFormElementDataList().size());

        List<String> formElementDataIdList = FormDataSOs.getFormElementDataIds(formDataSOReturn);
        assertTrue(formElementDataIdList.contains("id1"));
        assertTrue(formElementDataIdList.contains("id2"));
        assertTrue(formElementDataIdList.contains("id3"));

        Optional<FormElementDataSO> formElementDataSOOptional = FormDataSOs.getFormElementDataById(formDataSOReturn, "id1");
        assertTrue(formElementDataSOOptional.isPresent());
        formElementDataSOReturn = formElementDataSOOptional.get();
        assertEquals("id1", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("some dummy text", formElementDataSOReturn.getFormElementValue());

        formElementDataSOOptional = FormDataSOs.getFormElementDataById(formDataSOReturn, "id2");
        assertTrue(formElementDataSOOptional.isPresent());
        formElementDataSOReturn = formElementDataSOOptional.get();
        assertEquals("id2", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("some other dummy text as update", formElementDataSOReturn.getFormElementValue());

        formElementDataSOOptional = FormDataSOs.getFormElementDataById(formDataSOReturn, "id3");
        assertTrue(formElementDataSOOptional.isPresent());
        formElementDataSOReturn = formElementDataSOOptional.get();
        assertEquals("id3", formElementDataSOReturn.getFormElementId());
        assertEquals("input", formElementDataSOReturn.getFormElementType());
        assertEquals("lorem ipsum", formElementDataSOReturn.getFormElementValue());
    }

}
