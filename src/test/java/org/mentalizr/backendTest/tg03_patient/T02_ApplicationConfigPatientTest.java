package org.mentalizr.backendTest.tg03_patient;

import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.ApplicationConfigService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSOX;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NewClassNamingConvention")
public class T02_ApplicationConfigPatientTest {

    private static TestContext testContext;
    private static Session session;

    private static Program program;
    private static Therapist therapist;
    private static Patient patient;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();

        session = new Session(testContext);
        session.loginAsAdmin();

        program = new ProgramTest(testContext);
        program.create();

        therapist = new Therapist01(testContext);
        therapist.create();

        patient = new Patient01(program, therapist, testContext);
        patient.create();

        session.logout();

        session.loginAsUser(patient.getUsername(), patient.getPassword());
    }

    @AfterAll
    public static void cleanup() throws TestEntityException {
        System.out.println("\n>>> Clean-up >>>");

        session.logout();
        session.loginAsAdmin();

        patient.delete();
        therapist.delete();
        program.delete();

        session.logout();
    }

    @Test
    void applicationConfig() {
        System.out.println("\n>>> application config >>>");

        ApplicationConfigPatientSO applicationConfigPatientSO;
        try {
            applicationConfigPatientSO = new ApplicationConfigService(testContext.getRestCallContext()).call();

            System.out.println(ApplicationConfigPatientSOX.toJsonWithFormatting(applicationConfigPatientSO));

            // There is no mechanism for binding a special project configuration in case of testing.
            // We are leaving this with some superficial tests:

            assertTrue(Strings.isSpecified(applicationConfigPatientSO.getLogo()));
            assertTrue(Strings.isSpecified(applicationConfigPatientSO.getName()));

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
