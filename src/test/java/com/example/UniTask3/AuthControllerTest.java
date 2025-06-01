package com.example.UniTask3;

import com.example.UniTask3.controller.AuthController.AuthRequest;
import com.example.UniTask3.controller.AuthController.RegisterRequest;
import com.example.UniTask3.model.User;
import com.example.UniTask3.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  private static final String USERNAME = "testuser";
  private static final String PASSWORD = "password123";
  private static final String ROLE_USER = "ROLE_USER";
  private static final String INVALID_ROLE = "INVALID_ROLE";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void testRegisterUserWithValidRole() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername(USERNAME);
    request.setPassword(PASSWORD);
    request.setRole(ROLE_USER);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("User registered successfully")));

    Optional<User> savedUser = userRepository.findByUsername(USERNAME);
    assertTrue(savedUser.isPresent(), "User should be saved in the database");
    assertTrue(passwordEncoder.matches(PASSWORD, savedUser.get().getPassword()), "Password should be encoded correctly");
    assertTrue(savedUser.get().getRoles().contains(ROLE_USER), "User should have correct role");
  }

  @Test
  void testRegisterUserWithInvalidRole() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername(USERNAME);
    request.setPassword(PASSWORD);
    request.setRole(INVALID_ROLE);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Invalid role")));
  }

  @Test
  void testRegisterUserWithExistingUsername() throws Exception {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword(passwordEncoder.encode("password123"));
    user.setRoles(Set.of("ROLE_USER"));
    userRepository.save(user);

    RegisterRequest request = new RegisterRequest();
    request.setUsername("testuser");
    request.setPassword("newpassword");
    request.setRole("ROLE_USER");

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Username is already taken")));
  }


  @Test
  void testLoginWithValidCredentials() throws Exception {
    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(passwordEncoder.encode(PASSWORD));
    user.setRoles(Set.of(ROLE_USER));
    userRepository.save(user);

    AuthRequest request = new AuthRequest();
    request.setUsername(USERNAME);
    request.setPassword(PASSWORD);

    String token = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andReturn().getResponse().getCookie("jwt").getValue();

    assertTrue(Pattern.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$", token),
        "Returned token should be a valid JWT");

  }

  @Test
  void testLoginWithInvalidCredentials() throws Exception {
    AuthRequest request = new AuthRequest();
    request.setUsername("nonexistent");
    request.setPassword("wrongpassword");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("Invalid username or password")));
  }

  @Test
  void testRegisterUserWithEmptyUsername() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("");
    request.setPassword(PASSWORD);
    request.setRole(ROLE_USER);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testRegisterUserWithEmptyPassword() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername(USERNAME);
    request.setPassword("");
    request.setRole(ROLE_USER);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testRegisterUserRoleIsCorrectlySet() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("roleUser");
    request.setPassword("password123");
    request.setRole("ROLE_USER");

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    User savedUser = userRepository.findByUsername("roleUser").orElseThrow();

    assertTrue(savedUser.getRoles().contains("ROLE_USER"));
    assertEquals(1, savedUser.getRoles().size());
  }



}
