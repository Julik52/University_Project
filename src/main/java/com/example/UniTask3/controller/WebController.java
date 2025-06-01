package com.example.UniTask3.controller;

import com.example.UniTask3.Service.UserService;
import com.example.UniTask3.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class WebController {

  private static final Logger logger = LoggerFactory.getLogger(WebController.class);

  private final UserService userService;

  private final JwtUtil jwtUtil;

  private final AuthenticationManager authenticationManager;

  @GetMapping("/register")
  public String showRegistrationForm() {
    logger.info("Show registration form");
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {
    logger.info("Web register attempt for user '{}'", username);
    try {
      userService.registerUser(username, password);
      logger.info("User '{}' registered successfully (web)", username);
      return "redirect:/login";
    } catch (RuntimeException e) {
      logger.error("Registration error for user '{}': {}", username, e.getMessage());
      model.addAttribute("error", e.getMessage());
      return "register";
    }
  }

  @GetMapping("/login")
  public String showLoginForm() {
    logger.info("Show login form");
    return "login";
  }

  @PostMapping("/login")
  public String loginUser(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletResponse response,
                          Model model) {
    logger.info("Web login attempt for user '{}'", username);
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      var user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

      String token = jwtUtil.generateToken(username);
      Cookie cookie = new Cookie("jwt", token);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      response.addCookie(cookie);
      logger.info("User '{}' logged in successfully (web)", username);

      if (user.getRoles().contains("ROLE_ADMIN")) {
        return "redirect:/admin/dashboard";
      } else {
        return "redirect:/dashboard";
      }
    } catch (RuntimeException e) {
      logger.warn("Web login failed for user '{}': {}", username, e.getMessage());
      model.addAttribute("error", "Invalid username or password");
      return "login";
    }
  }
}
