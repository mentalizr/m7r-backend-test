package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.admin.formData.FormDataGetAllService;
import org.mentalizr.client.restService.generic.HtmlChunkService;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restService.patient.FormDataSaveService;
import org.mentalizr.client.restService.userAdmin.PatientGetAllService;
import org.mentalizr.client.restService.userAdmin.PatientGetService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class T05_00_RejectUnauthorizedAllMethodsBase {

    public static TestContext testContext;

    // Add all methods here that should only not be accessible in session state VALID.

    @Test
    @Order(2)
    void getPatient() {
        System.out.println("\n>>> getPatient >>>");
        try {
            new PatientGetService(
                    "dummy",
                    testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(3)
    void getAllPatients() {
        System.out.println("\n>>> getPatient >>>");
        try {
            new PatientGetAllService(
                    testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(4)
    void FormDataSave() {
        System.out.println("\n>>> formDataSave >>>");

        String contentId = "test_m1_sm1_somePage";

        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId("dummy");
        formDataSO.setContentId(contentId);

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
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(5)
    void formDataGet() {
        System.out.println("\n>>> formDataGet >>>");
        try {
            new FormDataGetService("dummy_content_id", testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(6)
    void formDataGetAll() {
        System.out.println("\n>>> formDataGetAll >>>");
        try {
            new FormDataGetAllService("dummy", testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(7)
    void chunkPatient() {
        System.out.println("\n>>> chunk PATIENT >>>");
        try {
            new HtmlChunkService("PATIENT", testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @Order(7)
    void chunkTherapist() {
        System.out.println("\n>>> chunk THERAPIST >>>");
        try {
            new HtmlChunkService("THERAPIST", testContext.getRestCallContext()).call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }


}
