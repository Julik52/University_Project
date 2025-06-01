package com.example.UniTask3.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DashboardController {

  private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

  @GetMapping("/dashboard")
  public String dashboard(Authentication authentication) {
    logger.info("User '{}' accessed /dashboard", authentication.getName());
    if (authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return "redirect:/admin/dashboard";
    }
    return "redirect:/user/dashboard";
  }

  @GetMapping("/admin/dashboard")
  public String adminDashboard() {
    logger.info("Accessed admin dashboard");
    return "admin_dashboard";
  }

  @GetMapping("/user/dashboard")
  public String userDashboard() {
    logger.info("Accessed user dashboard");
    return "user_dashboard";
  }
}

