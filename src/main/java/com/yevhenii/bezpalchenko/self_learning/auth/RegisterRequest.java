package com.yevhenii.bezpalchenko.self_learning.auth;

import com.yevhenii.bezpalchenko.self_learning.Model.User.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
}
