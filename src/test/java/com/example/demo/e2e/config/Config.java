package com.example.demo.e2e.config;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import com.example.demo.e2e.pojo.AccountCredentialsVO;
import com.example.demo.e2e.pojo.TokenVO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.lessThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Config {

//    @Value("${local.server.port}")
//    private Integer localPort;

    @LocalServerPort
    private Integer port;

    public static ObjectMapper objectMapper;
    public String accessToken;

    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32");

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void setup() {
        System.out.println("Local Port: " + port);
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(port)
                .setBasePath("/api")
                .setContentType("application/json")
                .addHeader("Accept", "application/json")
                //.addHeader(IntegrationTestUtil.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // will log everything in the request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // will log everything in the response
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                //.expectStatusCode(200)
                .expectResponseTime(lessThan(10000L))
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        accessToken = given()
                .basePath("/auth/signin")
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().as(TokenVO.class).getAccessToken();

        RestAssured.requestSpecification.header(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken);
    }
}
