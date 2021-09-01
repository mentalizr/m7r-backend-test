package org.mentalizr.backendTest.userManagement;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.backendTest.TestContext;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.userAdmin.*;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_CreateDeleteTherapistTest {

    private static final boolean active = true;
    private static final String username = "autotest_therapist_01";
    private static final String title = "Dr. med.";
    private static final String firstname = "therapist_01_firstname";
    private static final String lastname = "therapist_02_lastname";
    private static final int gender = 0;
    private static final String email = "thera01@example.org";
    private static final String password = "super_secret";
    private static String id;
    private static String passwordHash;

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
    void createTherapist() {
        System.out.println("### createTherapist ###");
        TherapistAddSO therapistAddSO = new TherapistAddSO();
        therapistAddSO.setActive(active);
        therapistAddSO.setUsername(username);
        therapistAddSO.setTitle(title);
        therapistAddSO.setFirstname(firstname);
        therapistAddSO.setLastname(lastname);
        therapistAddSO.setGender(gender);
        therapistAddSO.setEmail(email);
        therapistAddSO.setPassword(password);

        try {
            TherapistAddSO therapistAddSOReturn
                    = new TherapistAddService(therapistAddSO, testContext.getRestCallContext()).call();
            passwordHash = therapistAddSOReturn.getPasswordHash();
            id = therapistAddSOReturn.getUuid();

            assertEquals(active, therapistAddSOReturn.isActive());
            assertEquals(username, therapistAddSOReturn.getUsername());
            assertEquals(title, therapistAddSOReturn.getTitle());
            assertEquals(firstname, therapistAddSOReturn.getFirstname());
            assertEquals(lastname, therapistAddSOReturn.getLastname());
            assertEquals(gender, therapistAddSOReturn.getGender());
            assertEquals(email, therapistAddSOReturn.getEmail());
            assertTrue(therapistAddSOReturn.hasUuid());
            assertEquals(password, therapistAddSOReturn.getPassword());
            assertTrue(Strings.isSpecified(therapistAddSOReturn.getPasswordHash()));

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertTherapistCreated() {
        System.out.println("### assert therapist created by calling 'getAll' method ###");

        try {
            TherapistRestoreCollectionSO therapistRestoreCollectionSO
                    = new TherapistGetAllService(testContext.getRestCallContext()).call();
            boolean found = false;
            for (TherapistRestoreSO therapistRestoreSO : therapistRestoreCollectionSO.getCollection()) {
                if (therapistRestoreSO.getUsername().equals("autotest_therapist_01")) {

                    assertEquals(active, therapistRestoreSO.isActive());
                    assertEquals(username, therapistRestoreSO.getUsername());
                    assertEquals(title, therapistRestoreSO.getTitle());
                    assertEquals(firstname, therapistRestoreSO.getFirstname());
                    assertEquals(lastname, therapistRestoreSO.getLastname());
                    assertEquals(gender, therapistRestoreSO.getGender());
                    assertEquals(email, therapistRestoreSO.getEmail());
                    assertEquals(id, therapistRestoreSO.getUuid());
                    assertEquals(passwordHash, therapistRestoreSO.getPasswordHash());

                    found = true;
                    break;
                }
            }
            assertTrue(found, "Created therapist not found");

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(4)
    void deleteTherapist() {
        System.out.println("### deleteProgram ###");
        try {
            new TherapistDeleteService("autotest_therapist_01", testContext.getRestCallContext()).call();
        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            System.out.println("ERROR >>> " + e.getMessage());
            fail(e);
        }
    }

    @Test
    @Order(5)
    void assertTherapistDeleted() {
        System.out.println("### assert therapist deleted by calling 'getAll' method ###");

        try {
            TherapistRestoreCollectionSO therapistRestoreCollectionSO
                    = new TherapistGetAllService(testContext.getRestCallContext()).call();
            boolean found = false;
            for (TherapistRestoreSO therapistRestoreSO : therapistRestoreCollectionSO.getCollection()) {
                if (therapistRestoreSO.getUsername().equals("autotest_therapist_01")) {
                    found = true;
                    break;
                }
            }
            assertFalse(found, "Created therapist not found");

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
