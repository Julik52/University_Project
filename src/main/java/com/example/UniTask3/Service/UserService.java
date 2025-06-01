package com.example.UniTask3.Service;

import com.example.UniTask3.model.User;
import com.example.UniTask3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(String username, String password) {
    return registerUserWithRoles(username, password, Set.of("ROLE_USER"));
  }

  public User registerUserWithRoles(String username, String password, Set<String> roles) {
    userRepository.findByUsername(username).ifPresent(u -> {
      throw new RuntimeException("Username is already taken");
    });

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(roles);

    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
