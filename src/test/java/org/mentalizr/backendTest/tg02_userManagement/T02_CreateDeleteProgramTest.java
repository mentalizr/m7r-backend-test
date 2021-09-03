package org.mentalizr.backendTest.tg02_userManagement;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_CreateDeleteProgramTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("### Setup ###");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("### Clean-up ###");

        session.logout();
    }

    @Test
    @Order(2)
    void addProgram() {
        program = new ProgramTest(testContext);
        try {
            program.create();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertCreated() {
        try {
            program.find();
        } catch (TestEntityException | TestEntityNotFoundException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void deleteProgram() {
        System.out.println("### deleteProgram ###");

        try {
            program.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(5)
    void assertProgramDeleted() {
        System.out.println("### assert Program deleted by calling 'getAll' method ###");

        Exception exception = assertThrows(TestEntityNotFoundException.class, () -> program.find());
        assertEquals("Program [test] not found.", exception.getMessage());
    }

}
