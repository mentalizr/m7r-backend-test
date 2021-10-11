package org.mentalizr.backendTest.documents;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.commons.Dates;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

public class FormDataSimple01 extends FormData {

    public FormDataSimple01(TestContext testContext) {
        super(testContext);
    }

    @Override
    public String getContentId() {
        return "testDummy_m1_sm1_page02";
    }

    @Override
    public ExerciseSO getExerciseSO() {
        return null;
    }

    @Override
    public List<FormElementDataSO> getFormElementDataSOList() {
        List<FormElementDataSO> formElementDataSOList = new ArrayList<>();
        FormElementDataSO formElementDataSO = new FormElementDataSO();
        formElementDataSO.setFormElementId("id1");
        formElementDataSO.setFormElementType("type");
        formElementDataSO.setFormElementValue("value");
        formElementDataSOList.add(formElementDataSO);
        return formElementDataSOList;
    }

    @Override
    public FeedbackSO getFeedbackSO() {
        return null;
    }
}
