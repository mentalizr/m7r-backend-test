package org.mentalizr.backendTest;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.*;
import org.mentalizr.client.restService.sessionManagement.LoginService;
import org.mentalizr.client.restService.sessionManagement.LogoutService;
import org.mentalizr.client.restService.userAdmin.*;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.userManagement.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class T03_CreateDeleteTherapistTest {

    private static TestContext testContext;
    private static TherapistAddSO therapistAddSOReturn;

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
        therapistAddSO.setActive(true);
        therapistAddSO.setUsername("autotest_therapist_01");
        therapistAddSO.setTitle("Dr. med");
        therapistAddSO.setFirstname("thera01_firstname");
        therapistAddSO.setLastname("thera02_lastname");
        therapistAddSO.setGender(0);
        therapistAddSO.setEmail("thera01@example.org");
        therapistAddSO.setPassword("super_secret");

        try {
            therapistAddSOReturn = new TherapistAddService(therapistAddSO, testContext.getRestCallContext()).call();

            assertEquals(therapistAddSO.isActive(), therapistAddSOReturn.isActive());
            assertEquals(therapistAddSO.getUsername(), therapistAddSOReturn.getUsername());
            assertEquals(therapistAddSO.getTitle(), therapistAddSOReturn.getTitle());
            assertEquals(therapistAddSO.getFirstname(), therapistAddSOReturn.getFirstname());
            assertEquals(therapistAddSO.getLastname(), therapistAddSOReturn.getLastname());
            assertEquals(therapistAddSO.getGender(), therapistAddSOReturn.getGender());
            assertEquals(therapistAddSO.getEmail(), therapistAddSOReturn.getEmail());
            assertTrue(therapistAddSOReturn.hasUuid());
            assertEquals(therapistAddSO.getPassword(), therapistAddSOReturn.getPassword());
            assertFalse(Strings.isSpecified(therapistAddSOReturn.getPasswordHash()));

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void assertTherapistCreated() {
        System.out.println("### assert therapist created by calling 'getAll' method ###");

        assertNotNull(therapistAddSOReturn, "therapistAddSOReturnn ist null!");

        try {
            TherapistRestoreCollectionSO therapistRestoreCollectionSO
                    = new TherapistGetAllService(testContext.getRestCallContext()).call();
            boolean found = false;
            for (TherapistRestoreSO therapistRestoreSO : therapistRestoreCollectionSO.getCollection()) {
                if (therapistRestoreSO.getUsername().equals("autotest_therapist_01")) {

                    assertEquals(therapistRestoreSO.isActive(), therapistAddSOReturn.isActive());
                    assertEquals(therapistRestoreSO.getUsername(), therapistAddSOReturn.getUsername());
                    assertEquals(therapistRestoreSO.getTitle(), therapistAddSOReturn.getTitle());
                    assertEquals(therapistRestoreSO.getFirstname(), therapistAddSOReturn.getFirstname());
                    assertEquals(therapistRestoreSO.getLastname(), therapistAddSOReturn.getLastname());
                    assertEquals(therapistRestoreSO.getGender(), therapistAddSOReturn.getGender());
                    assertEquals(therapistRestoreSO.getEmail(), therapistAddSOReturn.getEmail());
                    assertEquals(therapistRestoreSO.getUuid(), therapistAddSOReturn.getUuid());
                    assertTrue(Strings.isSpecified(therapistAddSOReturn.getPasswordHash()));

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
