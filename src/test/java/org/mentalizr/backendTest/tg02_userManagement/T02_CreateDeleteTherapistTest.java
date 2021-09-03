package org.mentalizr.backendTest.tg02_userManagement;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

import static org.junit.jupiter.api.Assertions.*;

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

            assertEquals(therapist.isActive(), therapistAddSOReturn.isActive());
            assertEquals(therapist.getUsername(), therapistAddSOReturn.getUsername());
            assertEquals(therapist.getTitle(), therapistAddSOReturn.getTitle());
            assertEquals(therapist.getFirstname(), therapistAddSOReturn.getFirstname());
            assertEquals(therapist.getLastname(), therapistAddSOReturn.getLastname());
            assertEquals(therapist.getGender(), therapistAddSOReturn.getGender());
            assertEquals(therapist.getEmail(), therapistAddSOReturn.getEmail());
            assertTrue(Strings.isSpecified(therapistAddSOReturn.getUuid()));
            assertEquals(therapist.getPassword(), therapistAddSOReturn.getPassword());
            assertTrue(Strings.isSpecified(therapistAddSOReturn.getPasswordHash()));

        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void assertTherapistCreated() {
        System.out.println("\n>>> assert therapist is created >>>");

        try {
            TherapistRestoreSO therapistRestoreSO = therapist.find();

            assertEquals(therapist.isActive(), therapistRestoreSO.isActive());
            assertEquals(therapist.getUsername(), therapistRestoreSO.getUsername());
            assertEquals(therapist.getTitle(), therapistRestoreSO.getTitle());
            assertEquals(therapist.getFirstname(), therapistRestoreSO.getFirstname());
            assertEquals(therapist.getLastname(), therapistRestoreSO.getLastname());
            assertEquals(therapist.getGender(), therapistRestoreSO.getGender());
            assertEquals(therapist.getEmail(), therapistRestoreSO.getEmail());
            assertEquals(therapist.getId(), therapistRestoreSO.getUuid());
            assertEquals(therapist.getPasswordHash(), therapistRestoreSO.getPasswordHash());

        } catch (TestEntityException | TestEntityNotFoundException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void deleteTherapist() {
        System.out.println("\n>>> delete therapist >>>");

        try {
            therapist.delete();
        } catch (TestEntityException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void assertTherapistDeleted() {
        System.out.println("\n>>> assert therapist is deleted >>>");

        Exception exception = assertThrows(TestEntityNotFoundException.class, () -> therapist.find());
        assertEquals("Therapist [" + therapist.getUsername() + "] not found.", exception.getMessage());
    }

}
