package com.example.demo.e2e.tests;

import com.example.demo.e2e.config.ConfigXML;
import com.example.demo.e2e.config.ConfigYML;
import com.example.demo.e2e.config.IntegrationTestUtil;
import com.example.demo.e2e.config.YMLMapper;
import com.example.demo.e2e.pojo.AccountCredentialsVO;
import com.example.demo.e2e.pojo.TokenVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYML extends ConfigYML {

    private static TokenVO tokenVO;
    @Test
    @Order(0)
    public void testSignin() throws IOException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        System.out.println("credentials = " + credentials.getUsername());
        System.out.println("credentials = " + credentials.getPassword());

        tokenVO = given()
                .basePath("/auth/signin")
                .body(credentials, objectMapper)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(TokenVO.class, objectMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(1)
    public void testRefresh() throws IOException {
        TokenVO newTokenVO = given()
                .basePath("/auth/refresh")
                .pathParam("username", tokenVO.getUsername())
                .header(IntegrationTestUtil.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract().body().as(TokenVO.class, objectMapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
