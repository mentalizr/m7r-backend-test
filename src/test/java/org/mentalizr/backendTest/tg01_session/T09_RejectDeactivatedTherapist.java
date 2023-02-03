package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T09_RejectDeactivatedTherapist {

    private static TestContext testContext;
    private static Session session;
    private static Therapist therapist;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        therapist = new Therapist03(testContext);
        therapist.create();

        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.loginAsAdmin();

        therapist.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("\n>>> login >>>");

        RestServiceHttpException e = Assertions.assertThrows(RestServiceHttpException.class, () -> new LoginService(
                therapist.getTherapistRestoreSO().getUsername(),
                therapist.getPassword(),
                testContext.getRestCallContext()
        ).call());

        assertEquals(401, e.getStatusCode());
    }

}
