package com.example.UniTask3.controller;

import com.example.UniTask3.Service.UserService;
import com.example.UniTask3.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    logger.info("Register attempt: username='{}', role='{}'", request.getUsername(), request.getRole());

    if (isEmpty(request.getUsername())) {
      logger.warn("Registration failed: empty username");
      return ResponseEntity.badRequest().body("Username cannot be empty");
    }
    if (isEmpty(request.getPassword())) {
      logger.warn("Registration failed: empty password for user '{}'", request.getUsername());
      return ResponseEntity.badRequest().body("Password cannot be empty");
    }

    if (!"ROLE_USER".equals(request.getRole()) && !"ROLE_ADMIN".equals(request.getRole())) {
      logger.warn("Registration failed: invalid role '{}'", request.getRole());
      return ResponseEntity.badRequest().body("Invalid role: " + request.getRole());
    }

    try {
      userService.registerUserWithRoles(
          request.getUsername(),
          request.getPassword(),
          Set.of(request.getRole())
      );
      logger.info("User '{}' registered successfully", request.getUsername());
      return ResponseEntity.ok("User registered successfully");
    } catch (RuntimeException e) {
      logger.error("Registration error for user '{}': {}", request.getUsername(), e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody AuthRequest request,
      HttpServletRequest httpRequest,
      HttpServletResponse response) {

    logger.info("Login attempt for user '{}'", request.getUsername());

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getUsername(),
              request.getPassword()
          )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      HttpSession session = httpRequest.getSession(true);
      session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

      String token = jwtUtil.generateToken(request.getUsername());

      Cookie cookie = new Cookie("jwt", token);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      cookie.setMaxAge((int) (jwtUtil.getJwtExpirationInMs() / 1000));
      response.addCookie(cookie);

      logger.info("User '{}' logged in successfully", request.getUsername());
      return ResponseEntity.ok().body("Login successful");

    } catch (AuthenticationException e) {
      logger.warn("Login failed for user '{}': invalid credentials", request.getUsername());
      return ResponseEntity.status(401).body("Invalid username or password");
    }
  }

  private boolean isEmpty(String s) {
    return s == null || s.trim().isEmpty();
  }

  @Data
  public static class AuthRequest {
    private String username;
    private String password;
  }

  @Data
  public static class RegisterRequest {
    private String username;
    private String password;
    private String role;
  }
}
