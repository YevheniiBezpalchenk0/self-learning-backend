package com.yevhenii.bezpalchenko.self_learning.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yevhenii.bezpalchenko.self_learning.Model.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("role")
  private Enum<Role> role;
}
