package com.getir.readingisgood.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "User name can not be empty!")
    private String username;

    @NotBlank(message = "Password can not be empty!")
    private String password;

    @NotBlank(message = "Email can not be empty!")
    private String email;

}
