package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;

public class ProgramTest extends Program {

    public ProgramTest(TestContext testContext) {
        super(testContext);
    }

    @Override
    public String getProgramId() {
        return "test";
    }

}
