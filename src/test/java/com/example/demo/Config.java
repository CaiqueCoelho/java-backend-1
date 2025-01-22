package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("pactbroker.properties")
public class Config {
    @Value("${pact.broker.auth.token}")
    private String pactBrokerToken;

    public String getPactBrokerToken() {
        return pactBrokerToken;
    }
}
