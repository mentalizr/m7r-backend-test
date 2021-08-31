package org.mentalizr.backendTest;

import org.junit.jupiter.api.*;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.userAdmin.ProgramAddService;
import org.mentalizr.client.restService.userAdmin.ProgramDeleteService;
import org.mentalizr.client.restService.userAdmin.ProgramGetAllService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.ProgramCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_CreateDeleteProgramTest {

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
        programSO.setProgramId("test");

        try {
            new ProgramAddService(programSO, testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertProgramCreated() {
        System.out.println("### assert Program created by calling 'getAll' method ###");

        try {
            ProgramCollectionSO programCollectionSO = new ProgramGetAllService(testContext.getRestCallContext()).call();
            boolean found = false;
            for (ProgramSO programSO : programCollectionSO.getCollection()) {
                if (programSO.getProgramId().equals("test")) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Created program not found");

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(4)
    void deleteProgram() {
        System.out.println("### deleteProgram ###");
        try {
            new ProgramDeleteService("test", testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(5)
    void assertProgramDeleted() {
        System.out.println("### assert Program deleted by calling 'getAll' method ###");

        try {
            ProgramCollectionSO programCollectionSO = new ProgramGetAllService(testContext.getRestCallContext()).call();
            boolean found = false;
            for (ProgramSO programSO : programCollectionSO.getCollection()) {
                if (programSO.getProgramId().equals("test")) {
                    found = true;
                    break;
                }
            }
            assertFalse(found, "Created program not found");

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(6)
    void logout() {
        System.out.println("### Logout ###");
        try {
            new LogoutService(testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

}
