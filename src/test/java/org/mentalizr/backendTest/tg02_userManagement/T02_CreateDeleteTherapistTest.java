package org.mentalizr.backendTest.tg02_userManagement;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T02_CreateDeleteTherapistTest {

    private static TestContext testContext;
    private static Session session;

    private static Therapist therapist;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println(">>> setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> clean-up >>>");

        session.logout();;
    }

    @Test
    @Order(1)
    void createTherapist() {
        System.out.println("\n>>> create therapist >>>");

        therapist = new Therapist01(testContext);
        try {
            TherapistAddSO therapistAddSOReturn = therapist.create();

            assertTrue(Strings.isSpecified(therapistAddSOReturn.getUserId()));
            assertEquals(therapist.getTherapistAddSO().isActive(), therapistAddSOReturn.isActive());
            assertEquals(therapist.getTherapistAddSO().isRequirePolicyConsent(), therapistAddSOReturn.isRequirePolicyConsent());
            assertEquals(therapist.getTherapistAddSO().getUsername(), therapistAddSOReturn.getUsername());
            assertEquals(therapist.getPassword(), therapistAddSOReturn.getPassword());
            assertTrue(Strings.isSpecified(therapistAddSOReturn.getPasswordHash()));
            assertEquals(therapist.getTherapistAddSO().getEmail(), therapistAddSOReturn.getEmail());
            assertEquals(therapist.getTherapistAddSO().getTitle(), therapistAddSOReturn.getTitle());
            assertEquals(therapist.getTherapistAddSO().getFirstname(), therapistAddSOReturn.getFirstname());
            assertEquals(therapist.getTherapistAddSO().getLastname(), therapistAddSOReturn.getLastname());
            assertEquals(therapist.getTherapistAddSO().getGender(), therapistAddSOReturn.getGender());
            assertEquals(therapist.getTherapistAddSO().isRequire2FA(), therapistAddSOReturn.isRequire2FA());
            assertEquals(therapist.getTherapistAddSO().isRequireEmailConfirmation(), therapistAddSOReturn.isRequireEmailConfirmation());
            assertEquals(therapist.getTherapistAddSO().isRequireRenewPassword(), therapistAddSOReturn.isRequireRenewPassword());

        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void assertTherapistCreatedByGet() {
        System.out.println("\n>>> assert therapist is created (get) >>>");

        try {
            TherapistRestoreSO therapistRestoreSO = therapist.get();
            assertEqualTherapistRestoreSO(therapist.getTherapistRestoreSO(), therapistRestoreSO);
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertTherapistCreatedByFind() {
        System.out.println("\n>>> assert therapist is created (find) >>>");

        try {
            TherapistRestoreSO therapistRestoreSO = therapist.find();
            assertEqualTherapistRestoreSO(therapist.getTherapistRestoreSO(), therapistRestoreSO);
        } catch (TestEntityException | TestEntityNotFoundException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void deleteTherapist() {
        System.out.println("\n>>> delete therapist >>>");

        try {
            therapist.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(5)
    void assertTherapistDeleted() {
        System.out.println("\n>>> assert therapist is deleted >>>");

        Exception exception = assertThrows(TestEntityNotFoundException.class, () -> therapist.find());
        assertEquals("Therapist [" + therapist.getUsername() + "] not found.", exception.getMessage());
    }

    private void assertEqualTherapistRestoreSO(TherapistRestoreSO expected, TherapistRestoreSO actual) {
        assertEquals(expected.isActive(), actual.isActive());
        assertEquals(expected.getFirstActive(), actual.getFirstActive());
        assertEquals(expected.getLastActive(), actual.getLastActive());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPasswordHash(), actual.getPasswordHash());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getFirstname(), actual.getFirstname());
        assertEquals(expected.getLastname(), actual.getLastname());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.isSecondFA(), actual.isSecondFA());
        assertEquals(expected.getEmailConfirmation(), actual.getEmailConfirmation());
        assertEquals(expected.getEmailConfToken(), actual.getEmailConfToken());
        assertEquals(expected.getEmailConfCode(), actual.getEmailConfCode());
        assertEquals(expected.isRenewPasswordRequired(), actual.isRenewPasswordRequired());
    }

}
