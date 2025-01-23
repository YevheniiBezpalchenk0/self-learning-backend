package com.yevhenii.bezpalchenko.self_learning.DTO;

import com.yevhenii.bezpalchenko.self_learning.Model.User.Role;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class UserDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
