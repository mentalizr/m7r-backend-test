package org.mentalizr.backendTest.tg03_patient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.*;
import org.mentalizr.client.restService.patient.ProgramService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSOX;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class T01_ProgramTest {

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

        patient = new Patient01(testContext);
        patient.create(therapist.getId(), program.getProgramId());

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
    void program() {
        System.out.println("\n>>> program >>>");

        ProgramSO programSO;
        try {
            programSO = new ProgramService(testContext.getRestCallContext()).call();

            String programSOAsJsonActual = ProgramSOX.toJsonWithFormatting(programSO);
            System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

            String programSOAsJsonExpected = "\n" +
                    "{\n" +
                    "    \"blocking\": true,\n" +
                    "    \"id\": \"test\",\n" +
                    "    \"infotexts\": [\n" +
                    "        {\n" +
                    "            \"id\": \"test__info_1\",\n" +
                    "            \"name\": \"Dummyinfotext1\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"test__info_10\",\n" +
                    "            \"name\": \"Infoseite Test1\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"test__info_11\",\n" +
                    "            \"name\": \"Zweite Infoseite-Test\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"test__info_2\",\n" +
                    "            \"name\": \"Dummyinfotext2\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"modules\": [\n" +
                    "        {\n" +
                    "            \"accessible\": true,\n" +
                    "            \"id\": \"test_m1\",\n" +
                    "            \"name\": \"Eingabefelder\",\n" +
                    "            \"submodules\": [\n" +
                    "                {\n" +
                    "                    \"accessible\": true,\n" +
                    "                    \"id\": \"test_m1_sm1\",\n" +
                    "                    \"name\": \"page scope\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm1_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm1_s2\",\n" +
                    "                            \"name\": \"Schritt 2\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"accessible\": true,\n" +
                    "                    \"id\": \"test_m1_sm2\",\n" +
                    "                    \"name\": \"generic program scope\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm2_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm2_s2\",\n" +
                    "                            \"name\": \"Schritt 2\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm2_s3\",\n" +
                    "                            \"name\": \"Schritt 3\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"accessible\": true,\n" +
                    "                    \"id\": \"test_m1_sm3\",\n" +
                    "                    \"name\": \"program scope\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm3_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm3_s2\",\n" +
                    "                            \"name\": \"Schritt 2\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm3_s3\",\n" +
                    "                            \"name\": \"Schritt 3\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"accessible\": true,\n" +
                    "                    \"id\": \"test_m1_sm4\",\n" +
                    "                    \"name\": \"program scope inc\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm4_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm4_s2\",\n" +
                    "                            \"name\": \"Schritt 2\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m1_sm4_s3\",\n" +
                    "                            \"name\": \"Schritt 3\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"accessible\": true,\n" +
                    "            \"id\": \"test_m2\",\n" +
                    "            \"name\": \"Dummy\",\n" +
                    "            \"submodules\": [\n" +
                    "                {\n" +
                    "                    \"accessible\": true,\n" +
                    "                    \"id\": \"test_m2_sm1\",\n" +
                    "                    \"name\": \"Erstes Submodul\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": true,\n" +
                    "                            \"exercise\": true,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m2_sm1_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"accessible\": false,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": true,\n" +
                    "                            \"id\": \"test_m2_sm1_s2\",\n" +
                    "                            \"name\": \"Schritt 2\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"accessible\": false,\n" +
                    "                    \"id\": \"test_m2_sm2\",\n" +
                    "                    \"name\": \"Zweites Submodul\",\n" +
                    "                    \"steps\": [\n" +
                    "                        {\n" +
                    "                            \"accessible\": false,\n" +
                    "                            \"exercise\": false,\n" +
                    "                            \"feedback\": false,\n" +
                    "                            \"id\": \"test_m2_sm2_s1\",\n" +
                    "                            \"name\": \"Schritt 1\"\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"name\": \"Test\"\n" +
                    "}";

            assertEquals(programSOAsJsonExpected, programSOAsJsonActual);

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
