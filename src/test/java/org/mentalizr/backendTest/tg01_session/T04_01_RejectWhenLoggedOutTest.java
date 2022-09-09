package org.mentalizr.backendTest.tg01_session;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.Session;
import org.mentalizr.backendTest.entities.TestEntityException;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_01_RejectWhenLoggedOutTest extends T04_00_RejectUnauthorizedAllMethodsBase {

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        T04_00_RejectUnauthorizedAllMethodsBase.testContext = TestContext.getInstance();

        Session session = new Session(testContext);
        session.logout();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");
    }

    @Test
    @Order(1)
    void status() {
        System.out.println("\n>>> session status >>>");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(SessionStatusSOs.isInvalid(sessionStatusSO));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    // find test cases in base class

}
