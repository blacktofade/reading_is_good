package com.getir.readingisgood.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "User name can not be empty!")
    private String username;

    @NotBlank(message = "Password can not be empty!")
    private String password;
}
