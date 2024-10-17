package com.example.demo.controllers;

import com.example.demo.data.vo.v1.security.AccountCredentialsVO;
import com.example.demo.data.vo.v1.security.TokenVO;
import com.example.demo.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @Operation(summary = "Authenticate an user and return a token")
    @PostMapping(value = "/signin", produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signIn(@RequestBody AccountCredentialsVO credentials) {
        if (checkCredentialsIsValid(credentials)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password");

        ResponseEntity<TokenVO> token = authServices.signIn(credentials);
        if(token == null || token.getBody() == null || token.getBody().getAccessToken() == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password");

        return token;
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}", produces = { "application/json", "application/xml", "application/x-yaml" }, consumes = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty() || username == null || username.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password");

        ResponseEntity<TokenVO> token = authServices.refreshToken(username, refreshToken);
        if(token == null || token.getBody() == null || token.getBody().getAccessToken() == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username/password");

        return token;
    }

    private static boolean checkCredentialsIsValid(AccountCredentialsVO credentials) {
        return credentials == null
                || credentials.getUsername() == null || credentials.getUsername().isEmpty()
                || credentials.getPassword() == null || credentials.getPassword().isEmpty();
    }
}
