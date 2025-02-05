package com.josevides.restApi.controller;

import com.josevides.restApi.dto.login.LoginRequest;
import com.josevides.restApi.dto.login.LoginResponse;
import com.josevides.restApi.dto.user.UserRequest;
import com.josevides.restApi.dto.user.UserResponse;
import com.josevides.restApi.services.UserService;
import com.josevides.restApi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    private final JWTUtil jwtUtil = new JWTUtil();

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){

        try {

            LoginResponse loginResponse = this.userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error en el login: " + e.getMessage());
        }

    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String jwt){

        try {

            if (!this.jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "SUPER")){
                return ResponseEntity.badRequest().body("Su rol no es valido.");
            }

            UserResponse userResponse = this.userService.create(userRequest);
            return ResponseEntity.ok(userResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al crear usuario" + e.getMessage());
        }

    }

}
