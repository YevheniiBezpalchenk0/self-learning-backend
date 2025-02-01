package com.yevhenii.bezpalchenko.self_learning.auth;

import com.yevhenii.bezpalchenko.self_learning.config.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.yevhenii.bezpalchenko.self_learning.Model.User.Role.USER;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final LogoutService logoutService;

  @PostMapping("/register")
  public void register(
      @RequestBody RegisterRequest request,
      HttpServletResponse response
  ) throws IOException {
    request.setRole(USER);
    System.out.println("register request:" + request);
    service.register(request, response);
  }
  @PostMapping("/authentificate")
  public void authenticate(
      @RequestBody AuthenticationRequest request,
      HttpServletResponse response
  ) throws IOException {
    System.out.println("login request:" + request);
    service.authenticate(request, response);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          @CookieValue(value = "refreshToken", required = false) String refreshToken,
          HttpServletResponse response
  ) throws IOException {

    System.out.println("refresh-token request:" + refreshToken);
    service.refreshToken(refreshToken, response);
  }

  @PostMapping("/logout")
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {

    System.out.println("logout request:" + request);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutService.logout(request, response, authentication);
    }

}
