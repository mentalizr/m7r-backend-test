package org.mentalizr.backendTest;

import org.mentalizr.cli.config.CliConfiguration;
import org.mentalizr.cli.config.CliConfigurationLoader;
import org.mentalizr.cli.config.CredentialFile;
import org.mentalizr.client.RESTCallContext;

public class TestContext {

    private final RESTCallContext restCallContext;
    private final CredentialFile credentialFile;

    public TestContext(boolean debug) {
        CliConfiguration cliConfiguration = CliConfigurationLoader.load();
        this.restCallContext = new RESTCallContext(cliConfiguration, debug);
        this.credentialFile = new CredentialFile();
    }

    public static TestContext getInstance() {
        return new TestContext(true);
    }

    public RESTCallContext getRestCallContext() {
        return this.restCallContext;
    }

    public String getUser() {
        return credentialFile.getUser();
    }

    public String getPassword() {
        return credentialFile.getPassword();
    }

}
