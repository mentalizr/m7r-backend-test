package org.mentalizr.backendTest.tg02_userManagement;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_CreateDeletePatientTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println(">>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> clean-up >>>");

        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    @Order(1)
    public void createPatient() {
        System.out.println("\n>>> create patient >>>");

        patient = new Patient01(testContext);
        try {
            PatientAddSO patientAddSOReturn = patient.create(therapist.getId(), program.getProgramId());

            assertEquals(patient.isActive(), patientAddSOReturn.isActive());
            assertEquals(patient.getUsername(), patientAddSOReturn.getUsername());
            assertEquals(patient.getFirstname(), patientAddSOReturn.getFirstname());
            assertEquals(patient.getLastname(), patientAddSOReturn.getLastname());
            assertEquals(patient.getGender(), patientAddSOReturn.getGender());
            assertEquals(patient.getEmail(), patientAddSOReturn.getEmail());
            assertTrue(Strings.isSpecified(patientAddSOReturn.getUuid()));
            assertEquals(patient.getPassword(), patientAddSOReturn.getPassword());
            assertTrue(Strings.isSpecified(patientAddSOReturn.getPasswordHash()));
            assertEquals(program.getProgramId(), patientAddSOReturn.getProgramId());
            assertEquals(patient.isBlocking(), patientAddSOReturn.isBlocking());
            assertEquals(therapist.getId(), patientAddSOReturn.getTherapistId());

        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void assertPatientCreatedByGet() {
        System.out.println("\n>>> assert patient is created (get) >>>");

        try {
            PatientRestoreSO patientRestoreSO = patient.get();

            assertEquals(patient.getId(), patientRestoreSO.getUuid());
            assertEquals(patient.isActive(), patientRestoreSO.isActive());
            assertEquals(patient.getUsername(), patientRestoreSO.getUsername());
            assertEquals(patient.getPasswordHash(), patientRestoreSO.getPasswordHash());
            assertEquals(patient.getEmail(), patientRestoreSO.getEmail());
            assertEquals(patient.getFirstname(), patientRestoreSO.getFirstname());
            assertEquals(patient.getLastname(), patientRestoreSO.getLastname());
            assertEquals(patient.getGender(), patientRestoreSO.getGender());
            assertEquals(patient.isBlocking(), patientRestoreSO.isBlocking());
            assertEquals(therapist.getId(), patientRestoreSO.getTherapistId());

        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertPatientCreatedByFind() {
        System.out.println("\n>>> assert patient is created (get) >>>");

        try {
            PatientRestoreSO patientRestoreSO = patient.find();

            assertEquals(patient.getId(), patientRestoreSO.getUuid());
            assertEquals(patient.isActive(), patientRestoreSO.isActive());
            assertEquals(patient.getUsername(), patientRestoreSO.getUsername());
            assertEquals(patient.getPasswordHash(), patientRestoreSO.getPasswordHash());
            assertEquals(patient.getEmail(), patientRestoreSO.getEmail());
            assertEquals(patient.getFirstname(), patientRestoreSO.getFirstname());
            assertEquals(patient.getLastname(), patientRestoreSO.getLastname());
            assertEquals(patient.getGender(), patientRestoreSO.getGender());
            assertEquals(patient.isBlocking(), patientRestoreSO.isBlocking());
            assertEquals(therapist.getId(), patientRestoreSO.getTherapistId());

        } catch (TestEntityException | TestEntityNotFoundException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    public void deletePatient() {
        System.out.println("\n>>> delete patient >>>");

        try {
            patient.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(5)
    void assertPatientDeleted() {
        System.out.println("\n>>> assert patient is deleted >>>");

        Exception exception = assertThrows(TestEntityException.class, () -> patient.get());
        assertEquals("Patient [" + patient.getUsername() + "] not found.", exception.getMessage());
    }

}
