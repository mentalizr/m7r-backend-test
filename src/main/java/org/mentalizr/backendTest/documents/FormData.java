package org.mentalizr.backendTest.documents;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.Patient;
import org.mentalizr.backendTest.entities.TestEntity;
import org.mentalizr.backendTest.entities.TestEntityException;
import org.mentalizr.client.restService.patient.FormDataSaveService;
import org.mentalizr.client.restService.patient.FormDataSendService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

public abstract class FormData extends TestEntity {

    public FormData(TestContext testContext) {
        super(testContext);
    }

    public abstract String getContentId();

    public abstract ExerciseSO getExerciseSO();

    public abstract List<FormElementDataSO> getFormElementDataSOList();

    public abstract FeedbackSO getFeedbackSO();

    public void save(Patient patient) throws TestEntityException {
        FormDataSO formDataSO = getFormDataSO(patient);
        try {
            new FormDataSaveService(formDataSO, this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public void send(Patient patient) throws TestEntityException {
        FormDataSO formDataSO = getFormDataSO(patient);
        try {
            new FormDataSendService(formDataSO, this.testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            throw new TestEntityException(e.getMessage(), e);
        }
    }

    public FormDataSO getFormDataSO(Patient patient) {
        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(patient.getId());
        formDataSO.setContentId(getContentId());
        if (getExerciseSO() != null) formDataSO.setExercise(getExerciseSO());
        if (getFormElementDataSOList() != null) {
            formDataSO.setFormElementDataList(getFormElementDataSOList());
        } else {
            formDataSO.setFormElementDataList(new ArrayList<>());
        }
        if (getFeedbackSO() != null) {
            formDataSO.setFeedback(getFeedbackSO());
        }
        return formDataSO;
    }

}
