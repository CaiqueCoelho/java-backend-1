package com.example.demo.e2e.config;

public interface PersonEndpoints {

    String ALL_PERSON = "/person";
    String PERSON_ID = "/person/{id}";
    String DISABLE_PERSON_ID = "/person/disable/{id}";
    String PERSON_BY_NAME = "person/findPersonByName/{firstName}";
}
