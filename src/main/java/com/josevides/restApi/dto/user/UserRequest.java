package com.josevides.restApi.dto.user;

import lombok.Data;

@Data
public class UserRequest {

    private String username;
    private String password;
    private String role;
    private Boolean status;

}
