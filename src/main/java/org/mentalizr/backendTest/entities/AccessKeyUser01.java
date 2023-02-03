package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;

public class AccessKeyUser01 extends AccessKeyUser {

    public AccessKeyUser01(Program program, Therapist therapist, TestContext testContext) {
        super(program, therapist, testContext);
    }

    @Override
    public AccessKeyCreateSO getAccessKeyCreateSO() {
        AccessKeyCreateSO accessKeyCreateSO = new AccessKeyCreateSO();
        accessKeyCreateSO.setNrOfKeys(1);
        accessKeyCreateSO.setActive(true);
        accessKeyCreateSO.setStartWith("");
        accessKeyCreateSO.setProgramId(this.program.getProgramId());
        accessKeyCreateSO.setTherapistId(this.therapist.getUserId());
        return accessKeyCreateSO;
    }

}
