package com.aura.gallery.controller;

import com.aura.gallery.model.User;
import com.aura.gallery.repository.UserRepository;
import com.aura.gallery.security.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtils jwtUtils;

    // ── Sign Up ──────────────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email already registered"));
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepo.save(user);
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    // ── Login ────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return userRepo.findByEmail(req.getEmail())
            .filter(u -> encoder.matches(req.getPassword(), u.getPassword()))
            .map(u -> {
                String token = jwtUtils.generateToken(u.getEmail(), u.getRole().name());
                return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getName(), u.getEmail(), u.getRole()));
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse()));
    }

    // ── DTOs ─────────────────────────────────────
    static class SignupRequest {
        @NotBlank String name;
        @Email @NotBlank String email;
        @Size(min=6) @NotBlank String password;
        @NotNull User.Role role;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
    }
    
    static class LoginRequest {
        @Email @NotBlank String email;
        @NotBlank String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    static class AuthResponse {
        String token; Long id; String name; String email; User.Role role;
        
        public AuthResponse() {}
        public AuthResponse(String token, Long id, String name, String email, User.Role role) {
            this.token = token;
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
    }
    
    static class ErrorResponse {
        String message;
        
        public ErrorResponse() {}
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
