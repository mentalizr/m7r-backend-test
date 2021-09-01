package org.mentalizr.backendTest.userManagement;

import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;

import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T04_CreateDeletePatientTest {

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
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("### Clean-up ###");

        therapist.delete();
        program.delete();

        EntityManagementSession entityManagementSession = new EntityManagementSession(testContext);
        entityManagementSession.logout();
    }

    @Test
    @Order(1)
    public void addPatient() {
        System.out.println("### Add Patient...");

        patient = new Patient01(testContext);
        try {
            patient.create(therapist.getId(), program.getProgramId());
        } catch (TestEntityException e) {
            fail(e);
        }


    }

    @Test
    @Order(3)
    public void deletePatient() {
        System.out.println("### Add Patient...");

        try {
            patient.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }


}
