package com.example.recipesharing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO
{

    @NotEmpty(message = "Username should not be empty")
    @NotBlank(message = "Username should not be blank")
    private String userName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @NotBlank(message = "Password should not be blank")
    private String password;
}
