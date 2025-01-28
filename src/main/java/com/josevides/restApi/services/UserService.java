package com.josevides.restApi.services;

import com.josevides.restApi.dao.UserRepository;
import com.josevides.restApi.dto.login.LoginRequest;
import com.josevides.restApi.dto.login.LoginResponse;
import com.josevides.restApi.dto.user.UserRequest;
import com.josevides.restApi.dto.user.UserResponse;
import com.josevides.restApi.model.User;
import com.josevides.restApi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest)throws Exception{

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Credenciales invalidas"));

        if (this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        {
            return new LoginResponse(this.jwtUtil.generateToken(user.getRole()), "Login exitoso");
        }else {
            throw new Exception("Credenciales invalidas");
        }

    }

    public UserResponse create(UserRequest userRequest) throws Exception{

        this.userRepository.findByUsername(userRequest.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("Username ya existe");
                });
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(
                this.passwordEncoder.encode(userRequest.getPassword())
        );
        user.setRole(userRequest.getRole());
        user.setStatus(true);

        this.userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getRole(), "Usuario creado");

    }

}
