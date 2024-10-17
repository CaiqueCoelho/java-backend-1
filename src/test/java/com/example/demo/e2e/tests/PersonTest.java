package com.example.demo.e2e.tests;

import com.example.demo.Startup;
import com.example.demo.e2e.config.Config;
import com.example.demo.e2e.config.PersonEndpoints;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.*;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@Epic("REST API Regression Testing using TestNG")
@Feature("Verify CRUID Operations on User module")
class PersonTest extends Config {

    @Test
     void getPerson() {
        Response response = given()
                .when()
                .get();

        System.out.println(response.getBody().asString());
    }

    @Test
    void createPerson() throws JSONException {
        JSONObject person = new JSONObject();
        person.put("firstName", "Douglas");
        person.put("lastName", "Adams");
        person.put("address", "asdas");
        person.put("gender", "Male");

        given()
                .body(person.toString())
                .when()
                .post(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body(matchesJsonSchemaInClasspath("PersonSchema.json"))
                .and()
                .assertThat().body("id", greaterThan(0));
    }

    @Test
    void createPersonWithJsonPath() throws JSONException {
        JSONObject person = new JSONObject();
        person.put("firstName", "Douglas");
        person.put("lastName", "Adams");
        person.put("address", "asdas");
        person.put("gender", "Male");

        Response response = given()
                .body(person.toString())
                .when()
                .post();

        assertThat(response.jsonPath().get("id"), greaterThan(0));
        assertThat(response.jsonPath().getString("firstName"), equalTo("Douglas"));
        Assertions.assertNotNull(response.jsonPath().getString("lastName"));
    }

    @org.testng.annotations.Test(description = "To create a new user", priority = 1)
    @Story("POST Request")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify the creation of a new user")
    void createPersonWithJsonPathFromJayway() throws JSONException {
        JSONObject person = new JSONObject();
        person.put("firstName", "Douglas");
        person.put("lastName", "Adams");
        person.put("address", "asdas");
        person.put("gender", "Male");

        Response response = given()
                .body(person.toString())
                .when()
                .post();

        System.out.println(response.getBody().asString());

        DocumentContext jsonContext = JsonPath.parse(response.getBody().asString());
        assertThat(jsonContext.read("$.id"), greaterThan(0));
    }

    // TODO: Testing how to get a list from a response
    // TODO: Testing how to get a item from a list from a response
}
