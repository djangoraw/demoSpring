package com.example.demo;

import com.example.demo.requests.UserLoginRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.responses.UserActionResponse;
import com.example.demo.responses.UserCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/api/user/check")
    public ResponseEntity<UserCheckResponse> checkUser(HttpServletRequest request) {
        return ResponseEntity.ok(this.authenticationService.checkUser(request));
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<UserActionResponse> loginUser(HttpServletRequest request, @RequestBody UserLoginRequest data) {
        return ResponseEntity.ok(this.authenticationService.loginUser(request, data));
    }

    @PostMapping("/api/user/logout")
    public ResponseEntity<UserActionResponse> logoutUser(HttpServletRequest request) {
        return ResponseEntity.ok(this.authenticationService.logoutUser(request));
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<UserActionResponse> registerUser(@RequestBody UserRegisterRequest data) {
        return ResponseEntity.ok(this.authenticationService.registerUser(data));
    }
}