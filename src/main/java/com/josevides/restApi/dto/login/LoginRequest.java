package com.josevides.restApi.dto.login;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
