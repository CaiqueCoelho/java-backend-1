package com.example.demo.services;

import com.example.demo.data.vo.v1.security.AccountCredentialsVO;
import com.example.demo.data.vo.v1.security.TokenVO;
import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity signIn(AccountCredentialsVO credentials) {
        try {
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = userRepository.findByUsername(username);
            TokenVO token = new TokenVO();
            if(user != null) {
                token = jwtTokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    public ResponseEntity refreshToken(String username, String refreshToken) {
            User user = userRepository.findByUsername(username);
            TokenVO tokenResponse = new TokenVO();
            if(user != null) {
                tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }
            return ResponseEntity.ok(tokenResponse);

    }
}
