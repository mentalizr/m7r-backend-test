package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.commons.TestContext;

public abstract class TestEntity {

    protected TestContext testContext;

    public TestEntity(TestContext testContext) {
        this.testContext = testContext;
    }

}
