package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.LoginAccessKeyService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T08_RejectDeactivatedAccessKey {

    private static TestContext testContext;
    private static Session session;
    private static Program program;
    private static Therapist therapist;
    private static AccessKeyUser accessKeyUser;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        accessKeyUser = new AccessKeyUserDeactive(program, therapist, testContext);

        accessKeyUser.create();

        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.loginAsAdmin();

        accessKeyUser.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("\n>>> login >>>");

        RestServiceHttpException e = Assertions.assertThrows(RestServiceHttpException.class,
                () -> new LoginAccessKeyService(
                        accessKeyUser.getAccessKey(),
                        testContext.getRestCallContext()
                ).call());

        assertEquals(401, e.getStatusCode());
    }

}
