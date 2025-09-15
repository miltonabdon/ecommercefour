package com.milton.ecommercefour.controller;

import com.milton.ecommercefour.security.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public record LoginRequest(String username, String password) {}

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        System.out.println("AuthController /ping reached");
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody(required = false) LoginRequest request) {
        if (request == null) {
            System.out.println("AuthController /login called with empty or invalid body");
            return ResponseEntity.badRequest().body(Map.of("error", "Request body must be JSON with 'username' and 'password'"));
        }

        System.out.println("Attempting login for user: " + request.username());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            Object principal = authentication.getPrincipal();
            String username;
            List<String> roles;
            if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
                roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority().replace("ROLE_", "")).collect(Collectors.toList());
            } else {
                username = authentication.getName();
                roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(r -> r.replace("ROLE_", "")).collect(Collectors.toList());
            }
            String token = jwtUtil.generateToken(username, roles);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("roles", roles);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
