package org.mentalizr.backendTest.tg02_userManagement;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_CreateDeleteProgramTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> clean-up >>>");

        session.logout();
    }

    @Test
    @Order(1)
    void addProgram() {
        System.out.println("\n>>> add program >>>");

        program = new ProgramTest(testContext);
        try {
            program.create();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void assertCreated() {
        System.out.println("\n>>> assert program is created >>>");

        try {
            program.find();
        } catch (TestEntityException | TestEntityNotFoundException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void deleteProgram() {
        System.out.println("\n>>> deleteProgram >>>");

        try {
            program.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void assertProgramDeleted() {
        System.out.println("\n>>> assert program is deleted >>>");

        Exception exception = assertThrows(TestEntityNotFoundException.class, () -> program.find());
        assertEquals("Program [test] not found.", exception.getMessage());
    }

}
