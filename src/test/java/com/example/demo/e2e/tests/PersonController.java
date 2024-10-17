package com.example.demo.e2e.tests;

import com.example.demo.e2e.config.Config;
import com.example.demo.e2e.config.IntegrationTestUtil;
import com.example.demo.e2e.config.PersonEndpoints;
import com.example.demo.e2e.pojo.AccountCredentialsVO;
import com.example.demo.e2e.pojo.PersonVO;
import com.example.demo.e2e.pojo.TokenVO;
import com.example.demo.e2e.pojo.WrapperPersonVO;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.RestAssuredConfig;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HeaderConfig.headerConfig;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonController extends Config {

    @Test
    @Order(1)
    public void testCreate() throws IOException {
        PersonVO personVO = mockPerson();

        PersonVO createdPerson = createPerson(personVO);

        //PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

        assertTrue(createdPerson.getId() > 0);
        assertEquals("asdas", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals("Douglas", createdPerson.getFirstName());
        assertEquals("Adams", createdPerson.getLastName());
    }

    private PersonVO createPerson(PersonVO personVO) {
        return given()
                .body(personVO)
                .when()
                .post(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(201)
                .extract()
                .body()
                //.asString()
                .as(PersonVO.class);
    }

    @Test
    @Order(2)
    public void getPersonInXmlAndValidateXMLSchema() throws IOException {
        PersonVO personVO = mockPerson();

        given()
                .when()
                .accept("application/xml")
                .get(PersonEndpoints.ALL_PERSON)
                .then()
                .body(RestAssuredMatchers.matchesXsdInClasspath("Persons.xsd"));
    }

    @Test
    @Order(3)
    public void testCreateWithWrongOrigin() throws IOException {
        PersonVO personVO = mockPerson();

        var content = given()
                .header("Origin", "http://localhost:8088")
                .body(personVO)
                .when()
                .post(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(403)
                        .extract()
                .body()
                .asString();

        Assertions.assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(4)
    public void testFindById() throws IOException {
        PersonVO personVO = mockPerson();

        var newPerson = given()
                .body(personVO)
                .when()
                .post(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(PersonVO.class);

        var getPerson = given()
                .pathParam("id", newPerson.getId())
                .when()
                .get(PersonEndpoints.PERSON_ID)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class);

        assertEquals(newPerson.getId(), getPerson.getId());
        assertEquals(newPerson.getAddress(), getPerson.getAddress());
        assertEquals(newPerson.getGender(), getPerson.getGender());
        assertEquals(newPerson.getFirstName(), getPerson.getFirstName());
        assertEquals(newPerson.getLastName(), getPerson.getLastName());
        assertEquals(newPerson.getEnabled(), true);
    }

    @Test
    @Order(5)
    public void getPersonInJSONAndValidateJSONSchema() throws IOException {
        PersonVO personVO = mockPerson();

        given()
                .when()
                .get(PersonEndpoints.ALL_PERSON)
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("PeopleSchema.json"));
    }

    @Test
    @Order(6)
    public void testUpdate() throws IOException {
        PersonVO mockPerson = mockPerson();
        PersonVO createdPerson = createPerson(mockPerson);
        createdPerson.setFirstName("Douglas Updated");

        var updatedPerson = given()
                .body(createdPerson)
                .when()
                .put(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class);

        //PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("asdas", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
        assertEquals("Douglas Updated", updatedPerson.getFirstName());
        assertEquals("Adams", updatedPerson.getLastName());
        assertEquals(createdPerson.getId(), updatedPerson.getId());
    }

    @Test
    @Order(8)
    public void testDelete() throws IOException {
        PersonVO mockPerson = mockPerson();
        PersonVO createdPerson = createPerson(mockPerson);

        given()
                .pathParam("id", createdPerson.getId())
                .when()
                .delete(PersonEndpoints.PERSON_ID)
                .then()
                .statusCode(204);
    }

    @Test
    @Order(9)
    public void findAll() throws IOException {

        WrapperPersonVO wrapperPersonVO = given()
                .when()
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("direction", "asc")
                .get(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<WrapperPersonVO>() {});

        var people = wrapperPersonVO.getEmbedded().getPersons();

        for (PersonVO personVO : people) {
            MatcherAssert.assertThat(personVO.getAddress(), not(emptyString()));
            assertFalse(personVO.getFirstName().isEmpty());
        }
    }

    @Test
    @Order(10)
    public void findAllWithoutTokenShouldGetForbidden() throws IOException {

        RestAssuredConfig.config().headerConfig(headerConfig().overwriteHeadersWithName(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION));

        given()
                .header(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION, "")
                .when()
                .get(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(403);
    }

    @Test
    @Order(11)
    public void disablePersonById() throws IOException {
        PersonVO personVO = mockPerson();

        var newPerson = given()
                .body(personVO)
                .when()
                .post(PersonEndpoints.ALL_PERSON)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(PersonVO.class);

        var getPerson = given()
                .pathParam("id", newPerson.getId())
                .when()
                .get(PersonEndpoints.PERSON_ID)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class);

        assertEquals(newPerson.getId(), getPerson.getId());
        assertEquals(newPerson.getAddress(), getPerson.getAddress());
        assertEquals(newPerson.getGender(), getPerson.getGender());
        assertEquals(newPerson.getFirstName(), getPerson.getFirstName());
        assertEquals(newPerson.getLastName(), getPerson.getLastName());
        assertEquals(newPerson.getEnabled(), true);

        var disabledPerson = given()
                .pathParam("id", newPerson.getId())
                .when()
                .patch(PersonEndpoints.DISABLE_PERSON_ID)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class);

        assertEquals(disabledPerson.getId(), getPerson.getId());
        assertEquals(disabledPerson.getAddress(), getPerson.getAddress());
        assertEquals(disabledPerson.getGender(), getPerson.getGender());
        assertEquals(disabledPerson.getFirstName(), getPerson.getFirstName());
        assertEquals(disabledPerson.getLastName(), getPerson.getLastName());
        assertEquals(disabledPerson.getEnabled(), false);
    }


    private PersonVO mockPerson() {
        PersonVO person = new PersonVO();
        person.setFirstName("Douglas");
        person.setLastName("Adams");
        person.setAddress("asdas");
        person.setGender("Male");
        person.setEnabled(true);
        return person;
    }
}
