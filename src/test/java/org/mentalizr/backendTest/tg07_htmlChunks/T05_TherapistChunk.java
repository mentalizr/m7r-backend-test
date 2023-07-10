package org.mentalizr.backendTest.tg07_htmlChunks;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.generic.HtmlChunkService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T05_TherapistChunk {

    private static TestContext testContext;
    private static Session session;
    private static Program program;
    private static Therapist therapist;
    private static Patient patient;


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

        patient = new Patient01(program, therapist, testContext);
        patient.create();

        session.logout();

        session.login(therapist);
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
        session.loginAsAdmin();

        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(2)
    void getChunk() {
        System.out.println("\n>>> THERAPIST chunk >>>");
        try {
            String chunk = new HtmlChunkService("THERAPIST", testContext.getRestCallContext()).call();
            assertTrue(chunk.trim().startsWith("<!-- Step Wrapper -->"));
            assertTrue(chunk.contains("id=\"patient-overview-template\""));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
