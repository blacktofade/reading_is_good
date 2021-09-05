package com.getir.readingisgood.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private List<String> roles;

    public AuthenticationResponse(String accessToken, String id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
