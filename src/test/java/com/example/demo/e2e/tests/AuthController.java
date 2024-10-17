package com.example.demo.e2e.tests;

import com.example.demo.e2e.config.Config;
import com.example.demo.e2e.config.IntegrationTestUtil;
import com.example.demo.e2e.pojo.AccountCredentialsVO;
import com.example.demo.e2e.pojo.TokenVO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthController extends Config {

    private static TokenVO tokenVO;

    @Test
    @Order(0)
    public void testSignin() throws IOException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
                .basePath("/auth/signin")
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(1)
    public void testRefresh() throws IOException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        TokenVO newTokenVO = given()
                .basePath("/auth/refresh")
                .pathParam("username", tokenVO.getUsername())
                .header(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract().body().as(TokenVO.class);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
