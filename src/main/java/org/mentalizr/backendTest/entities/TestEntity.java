package org.mentalizr.backendTest.entities;

import org.mentalizr.backendTest.TestContext;

public abstract class TestEntity {

    protected TestContext testContext;

    public TestEntity(TestContext testContext) {
        this.testContext = testContext;
    }

}
