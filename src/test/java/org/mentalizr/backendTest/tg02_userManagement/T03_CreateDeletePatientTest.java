package org.mentalizr.backendTest.tg02_userManagement;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
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

        patient = new Patient01(program, therapist, testContext);
        try {
            PatientAddSO patientAddSOReturn = patient.create();

            assertEquals(patient.getPatientAddSO().isActive(), patientAddSOReturn.isActive());
            assertEquals(patient.getPatientAddSO().isRequirePolicyConsent(), patientAddSOReturn.isRequirePolicyConsent());
            assertEquals(patient.getPatientAddSO().getUsername(), patientAddSOReturn.getUsername());
            assertEquals(patient.getPatientAddSO().getEmail(), patientAddSOReturn.getEmail());
            assertTrue(Strings.isSpecified(patientAddSOReturn.getUserId()));
            assertEquals(patient.getPassword(), patientAddSOReturn.getPassword());
            assertTrue(Strings.isSpecified(patientAddSOReturn.getPasswordHash()));
            assertEquals(patient.getPatientAddSO().getFirstname(), patientAddSOReturn.getFirstname());
            assertEquals(patient.getPatientAddSO().getLastname(), patientAddSOReturn.getLastname());
            assertEquals(patient.getPatientAddSO().getGender(), patientAddSOReturn.getGender());
            assertEquals(patient.getPatientAddSO().isRequire2FA(), patientAddSOReturn.isRequire2FA());
            assertEquals(patient.getPatientAddSO().isRequireEmailConfirmation(), patientAddSOReturn.isRequireEmailConfirmation());
            assertEquals(patient.getPatientAddSO().isRequireRenewPassword(), patientAddSOReturn.isRequireRenewPassword());
            assertEquals(program.getProgramId(), patientAddSOReturn.getProgramId());
            assertEquals(patient.getPatientAddSO().isBlocking(), patientAddSOReturn.isBlocking());
            assertEquals(therapist.getUserId(), patientAddSOReturn.getTherapistId());

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
            assertEquals(patient.getPatientRestoreSO(), patientRestoreSO);

//            assertEquals(patient.getPatientAddSO().getUserId(), patientRestoreSO.getUserId());
//            assertEquals(patient.getPatientAddSO().isActive(), patientRestoreSO.isActive());
//            assertEquals(patient.getPatientAddSO().getUsername(), patientRestoreSO.getUsername());
//            assertEquals(patient.getPatientAddSO().getPasswordHash(), patientRestoreSO.getPasswordHash());
//            assertEquals(patient.getPatientAddSO().getEmail(), patientRestoreSO.getEmail());
//            assertEquals(patient.getPatientAddSO().getFirstname(), patientRestoreSO.getFirstname());
//            assertEquals(patient.getPatientAddSO().getLastname(), patientRestoreSO.getLastname());
//            assertEquals(patient.getPatientAddSO().getGender(), patientRestoreSO.getGender());
//            assertEquals(patient.getPatientAddSO().isBlocking(), patientRestoreSO.isBlocking());
//            assertEquals(therapist.getId(), patientRestoreSO.getTherapistId());

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
            assertEquals(patient.getPatientRestoreSO(), patientRestoreSO);

//            assertEquals(patient.getId(), patientRestoreSO.getUserId());
//            assertEquals(patient.isActive(), patientRestoreSO.isActive());
//            assertEquals(patient.getUsername(), patientRestoreSO.getUsername());
//            assertEquals(patient.getPasswordHash(), patientRestoreSO.getPasswordHash());
//            assertEquals(patient.getEmail(), patientRestoreSO.getEmail());
//            assertEquals(patient.getFirstname(), patientRestoreSO.getFirstname());
//            assertEquals(patient.getLastname(), patientRestoreSO.getLastname());
//            assertEquals(patient.getGender(), patientRestoreSO.getGender());
//            assertEquals(patient.isBlocking(), patientRestoreSO.isBlocking());
//            assertEquals(therapist.getId(), patientRestoreSO.getTherapistId());

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

        TestEntityException exception = assertThrows(TestEntityException.class, () -> patient.get());
        assertTrue(exception.hasStatusCode());
        assertEquals(470, exception.getStatusCode());
    }

}
