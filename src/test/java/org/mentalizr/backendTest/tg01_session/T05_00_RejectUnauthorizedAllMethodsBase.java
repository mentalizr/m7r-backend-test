package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.client.restService.RestService;
import org.mentalizr.client.restService.admin.formData.FormDataGetAllService;
import org.mentalizr.client.restService.generic.HtmlChunkService;
import org.mentalizr.client.restService.patient.FormDataGetService;
import org.mentalizr.client.restService.patient.FormDataSaveService;
import org.mentalizr.client.restService.userAdmin.PatientGetAllService;
import org.mentalizr.client.restService.userAdmin.PatientGetService;
import org.mentalizr.client.restService.userAdmin.UserActivateService;
import org.mentalizr.client.restService.userAdmin.UserDeactivateService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class T05_00_RejectUnauthorizedAllMethodsBase {

    public static TestContext testContext;

    protected void assertUnauthorizedException(RestService service) {
        try {
            service.call();
        } catch (RestServiceConnectionException e) {
            fail(e);
        } catch (RestServiceHttpException e) {
            assertEquals(401, e.getStatusCode());
        }
    }

    // Add all methods here that should only not be accessible in session state VALID.

    @Test
    @Order(2)
    void getPatient() {
        System.out.println("\n>>> getPatient >>>");
        assertUnauthorizedException(new PatientGetService("dummy", testContext.getRestCallContext()));
    }

    @Test
    @Order(3)
    void getAllPatients() {
        System.out.println("\n>>> getPatient >>>");
        assertUnauthorizedException(new PatientGetAllService(testContext.getRestCallContext()));
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
        assertUnauthorizedException(new FormDataGetService("dummy_content_id", testContext.getRestCallContext()));
    }

    @Test
    @Order(6)
    void formDataGetAll() {
        System.out.println("\n>>> formDataGetAll >>>");
        assertUnauthorizedException(new FormDataGetAllService("dummy", testContext.getRestCallContext()));
    }

    @Test
    @Order(7)
    void chunkPatient() {
        System.out.println("\n>>> chunk PATIENT >>>");
        assertUnauthorizedException(new HtmlChunkService("PATIENT", testContext.getRestCallContext()));
    }

    @Test
    @Order(8)
    void chunkTherapist() {
        System.out.println("\n>>> chunk THERAPIST >>>");
        assertUnauthorizedException(new HtmlChunkService("THERAPIST", testContext.getRestCallContext()));
    }

    @Test
    @Order(9)
    void deactivateUser() {
        System.out.println("\n>>> deactivate user >>>");
        assertUnauthorizedException(new UserDeactivateService("dummy", testContext.getRestCallContext()));
    }

    @Test
    @Order(9)
    void activateUser() {
        System.out.println("\n>>> activate user >>>");
        assertUnauthorizedException(new UserActivateService("dummy", testContext.getRestCallContext()));
    }

}
