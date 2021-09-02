package org.mentalizr.backendTest.patient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.ProgramService;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.userAdmin.ProgramAddService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSOX;

import static org.junit.jupiter.api.Assertions.fail;

public class T01_ProgramTest {

    private static TestContext testContext;
    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("### Setup ###");

        testContext = TestContext.getInstance();

        EntityManagementSession entityManagementSession = new EntityManagementSession(testContext);
        entityManagementSession.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        patient = new Patient01(testContext);
        patient.create(therapist.getId(), program.getProgramId());

        entityManagementSession.logout();

        entityManagementSession.loginAsUser(patient.getUsername(), patient.getPassword());
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("### Clean-up ###");

        EntityManagementSession entityManagementSession = new EntityManagementSession(testContext);
        entityManagementSession.logout();
        entityManagementSession.loginAsAdmin();

        patient.delete();
        therapist.delete();
        program.delete();

        entityManagementSession.logout();
    }

    @Test
    void program() {
        System.out.println("### program ###");

        ProgramSO programSO;
        try {
            programSO = new ProgramService(testContext.getRestCallContext()).call();
            System.out.println(ProgramSOX.toJson(programSO));
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }



    }

//    @Test
//    @Order(1)
//    void login() {
//        System.out.println("### Login ###");
//        try {
//            new LoginService(testContext.getUser(), testContext.getPassword(), testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            System.out.println("ERROR >>> " + e.getMessage());
//            fail(e);
//        }
//    }
//
//    @Test
//    @Order(2)
//    void addProgram() {
//        System.out.println("### createProgram ###");
//        ProgramSO programSO = new ProgramSO();
//        programSO.setProgramId(programName);
//
//        try {
//            new ProgramAddService(programSO, testContext.getRestCallContext()).call();
//        } catch (RestServiceHttpException | RestServiceConnectionException e) {
//            System.out.println("ERROR >>> " + e.getMessage());
//            fail(e);
//        }
//    }
//
//    @Test
//    @Order(3)
//    void getProgram() {
//        System.out.println("### patient/program ###");
//
//
//    }

}
