package org.mentalizr.backendTest.tg02_userManagement;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.documents.FormData;
import org.mentalizr.backendTest.documents.FormDataExercise01;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.userAdmin.UserGetActivityService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.requestObjects.ActivityCommandSO;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSOX;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_CreateActivityStatus {
    private static TestContext testContext;
    private static long startTimestamp;
    private static Session session;
    private static  Program program;
    private static Therapist therapist;
    private static Patient patient;
    private static FormData formData;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println(">>> Setup >>>");
        testContext = TestContext.getInstance();
        startTimestamp = System.currentTimeMillis();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        patient = new Patient01(program, therapist, testContext);
        patient.create();

        session.logout();
        session.login(patient);

        //Insert the data
        formData = new FormDataExercise01(testContext);
        formData.send(patient);

        session.logout();
        session.loginAsAdmin();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> clean-up >>>");
        session.logout();
        session.loginAsAdmin();
        patient.cleanFormData();
        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    void getAll() throws TestEntityException {
        System.out.println("\n>>> getAll >>>");
        session.logout();
        session.loginAsAdmin();

        ActivityStatusMessageCollectionSO messageCollectionSO;
        ActivityCommandSO commandSO = new ActivityCommandSO();
        commandSO.setUserId(patient.getUserId());
        commandSO.setFromTimestamp(startTimestamp);
        commandSO.setUntilTimestamp(System.currentTimeMillis());

        try {
            messageCollectionSO = new UserGetActivityService(commandSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("Collection Size: " + messageCollectionSO.getCollection().size());
        System.out.println(ActivityStatusMessageCollectionSOX.toJsonWithFormatting(messageCollectionSO));

        assertEquals(1, messageCollectionSO.getCollection().size());
    }

}
