package org.mentalizr.backendTest.documents;

import org.mentalizr.backendTest.commons.Dates;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;

import java.util.ArrayList;
import java.util.List;

public class FormDataExercise01 extends FormData {

    public FormDataExercise01(TestContext testContext) {
        super(testContext);
    }

    @Override
    public String getContentId() {
        return "test_m1_sm1_page01";
    }

    @Override
    public ExerciseSO getExerciseSO() {
        ExerciseSO exerciseSO = new ExerciseSO();
        exerciseSO.setSent(false);
        exerciseSO.setLastModifiedTimestamp("2021-09-09T15:15:00Z");
        exerciseSO.setSeenByTherapist(false);
        exerciseSO.setSeenByTherapistTimestamp(Dates.epochAsISO());
        return exerciseSO;
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
