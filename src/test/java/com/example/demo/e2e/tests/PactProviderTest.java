package com.example.demo.e2e.tests;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import com.example.demo.e2e.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("api-person-provider")
@PropertySource("pactbroker.properties")
@PactBroker(
        url = "https://omega.pactflow.io/",
        authentication = @PactBrokerAuth(token = "${pact.broker.auth.token}")
)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PactProviderTest extends Config {

    @LocalServerPort
    public int port;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTest(PactVerificationContext context)
    {
        context.verifyInteraction();
    }

    @BeforeEach
    public void setup(PactVerificationContext context)
    {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State("passes")
    public void toPassesState() {
        // Set up your provider state here, e.g., mock or set up data in the database
        System.out.println("Setting up provider state for 'passes'");
        // Your setup logic here, such as creating necessary entities in the database
    }

    @State("")
    public void toState() {
        // Set up your provider state here, e.g., mock or set up data in the database
        System.out.println("Setting up provider state for 'passes'");
        // Your setup logic here, such as creating necessary entities in the database
    }
}
