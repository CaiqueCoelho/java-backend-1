package com.example.demo.e2e.config;

import com.example.demo.e2e.pojo.AccountCredentialsVO;
import com.example.demo.e2e.pojo.TokenVO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigYML {

//    @Value("${local.server.port}")
//    private Integer localPort;
    public static YMLMapper objectMapper;

    @LocalServerPort
    private Integer port;

    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32");

    @BeforeAll
    static void beforeAll() {
        objectMapper = new YMLMapper();
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
                .setContentType(IntegrationTestUtil.CONTENT_TYPE_YML)
                .setAccept(IntegrationTestUtil.CONTENT_TYPE_YML)
                .addHeader("Accept", IntegrationTestUtil.CONTENT_TYPE_YML)
                //.addHeader(IntegrationTestUtil.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // will log everything in the request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // will log everything in the response
                .setConfig(
                        RestAssured
                                .config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        IntegrationTestUtil.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                //.expectStatusCode(200)
                .expectResponseTime(lessThan(5000L))
                .build();

//        objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        System.out.println("credentials = " + credentials.toString());

        var accessToken = given()
                .log().all()
                .basePath("/auth/signin")
                .body(credentials.toString())
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(TokenVO.class, objectMapper).getAccessToken();

        RestAssured.requestSpecification.header(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken);
    }
}
