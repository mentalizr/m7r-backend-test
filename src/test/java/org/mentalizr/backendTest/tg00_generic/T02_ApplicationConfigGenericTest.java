package org.mentalizr.backendTest.tg00_generic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backendTest.commons.TestContext;
import org.mentalizr.backendTest.entities.TestEntityException;
import org.mentalizr.client.restService.generic.ApplicationConfigGenericService;
import org.mentalizr.client.restServiceCaller.exception.RestServiceConnectionException;
import org.mentalizr.client.restServiceCaller.exception.RestServiceHttpException;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSOX;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("NewClassNamingConvention")
public class T02_ApplicationConfigGenericTest {

    private static TestContext testContext;

    @BeforeAll
    public static void setup() throws TestEntityException {
        System.out.println("\n>>> Setup >>>");

        testContext = TestContext.getInstance();
    }

    @Test
    void applicationConfig() {
        System.out.println("\n>>> application config >>>");

        ApplicationConfigGenericSO applicationConfigGenericSO;
        try {
            applicationConfigGenericSO = new ApplicationConfigGenericService(testContext.getRestCallContext()).call();

            System.out.println(ApplicationConfigGenericSOX.toJsonWithFormatting(applicationConfigGenericSO));

            assertEquals("vbox-m7r-dev", applicationConfigGenericSO.getDomainName());
            assertEquals("mentalizr", applicationConfigGenericSO.getTitle());
            assertEquals("LOGO_mentalizr.png", applicationConfigGenericSO.getLogo());

        } catch (RestServiceHttpException | RestServiceConnectionException e) {
            fail(e);
        }
    }

}
