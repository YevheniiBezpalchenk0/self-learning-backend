package com.yevhenii.bezpalchenko.self_learning.auth;

import com.yevhenii.bezpalchenko.self_learning.Model.User.User;
import com.yevhenii.bezpalchenko.self_learning.Model.User.UserRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Token.Token;
import com.yevhenii.bezpalchenko.self_learning.Model.Token.TokenRepository;
import com.yevhenii.bezpalchenko.self_learning.Model.Token.TokenType;
import com.yevhenii.bezpalchenko.self_learning.config.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public void register(RegisterRequest request, HttpServletResponse response) throws IOException {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/"); // Доступен для всего приложения
    cookie.setMaxAge(7 * 24 * 60 * 60); // Примерно 1 неделя
    response.addCookie(cookie);
    saveUserToken(savedUser, jwtToken);

    var authResponse = AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .role(user.getRole())
        .build();
    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
  }

  public void authenticate(AuthenticationRequest request, HttpServletResponse response) throws IOException {
    System.out.println(request.getEmail() + " " + request.getPassword());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(cookie);
    var authResponse = AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .role(user.getRole())
        .build();
    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          String refreshToken,
          HttpServletResponse response
  ) throws IOException {
    if (refreshToken == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow(() -> new RuntimeException("User not found"));

      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, accessToken);

        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .role(user.getRole())
                .build();

        // Установка refresh токена в куки (HttpOnly)
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
