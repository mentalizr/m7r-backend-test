package org.mentalizr.backendTest.tg07_htmlChunks;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.TestEntityException;
import org.mentalizr.client.restService.generic.HtmlChunkService;
import org.mentalizr.client.restService.sessionManagement.SessionStatusService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.mentalizr.serviceObjects.SessionStatusSOs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_LoginVoucherChunk {

    private static TestContext testContext;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();
    }

    @Test
    @Order(1)
    void statusPreConsent() {
        System.out.println("\n>>> session status >>>");
        try {
            SessionStatusSO sessionStatusSO = new SessionStatusService(testContext.getRestCallContext()).call();
            assertTrue(SessionStatusSOs.isInvalid(sessionStatusSO));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void consent() {
        System.out.println("\n>>> LOGIN_VOUCHER chunk >>>");
        try {
            String chunk = new HtmlChunkService("LOGIN_VOUCHER", testContext.getRestCallContext()).call();
            assertTrue(chunk.trim().startsWith("<div class=\"container"));
            assertTrue(chunk.contains("id=\"login-voucher--form\""));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
