package org.mentalizr.backendTest.patient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.userAdmin.ProgramAddService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * TODO WIP
 */
public class T01_ProgramTest {

    private static final String programName = "test";


    private static TestContext testContext;

    @BeforeAll
    public static void setup() {
        System.out.println("### Setup ###");
        testContext = TestContext.getInstance();
    }

    @Test
    @Order(1)
    void login() {
        System.out.println("### Login ###");
        try {
            new LoginService(testContext.getUser(), testContext.getPassword(), testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(2)
    void addProgram() {
        System.out.println("### createProgram ###");
        ProgramSO programSO = new ProgramSO();
        programSO.setProgramId(programName);

        try {
            new ProgramAddService(programSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(3)
    void getProgram() {
        System.out.println("### patient/program ###");


    }


}
