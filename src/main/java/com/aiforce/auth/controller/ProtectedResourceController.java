package com.aiforce.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedResourceController {

    @GetMapping("/account")
    public String account(Authentication authentication) {
        return "Protected account features are accessible for " + authentication.getName();
    }
}
