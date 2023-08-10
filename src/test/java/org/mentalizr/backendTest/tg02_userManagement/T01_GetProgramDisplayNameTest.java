package org.mentalizr.backendTest.tg02_userManagement;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.userAdmin.ProgramGetDisplayNameService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T01_GetProgramDisplayNameTest {

    private static TestContext testContext;
    private static Session session;
    private static Program program;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> clean-up >>>");

        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void getProgramDisplayName() {
        System.out.println("\n>>> getDisplayName >>>");

        String programId = program.getProgramId();
        try {
            String programDisplayName = new ProgramGetDisplayNameService(programId, testContext.getRestCallContext()).call();
            assertEquals("Test", programDisplayName);
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
