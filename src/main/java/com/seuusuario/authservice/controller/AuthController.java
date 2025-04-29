package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.dto.LoginRequest;
import com.seuusuario.authservice.dto.RegisterRequest;
import com.seuusuario.authservice.dto.UserResponse;
import com.seuusuario.authservice.entity.User;
import com.seuusuario.authservice.repository.UserRepository;
import com.seuusuario.authservice.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository; 

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        Optional<User> userOpt = userRepository.findById(idUsuario);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        // Convertendo para o DTO de resposta
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setNome(user.getNome());
        userResponse.setSobrenome(user.getSobrenome());
        userResponse.setEmail(user.getEmail());
        userResponse.setCelular(user.getCelular());
        userResponse.setCpf(user.getCpf());
        userResponse.setCodigoMercadoPago(user.getCodigoMercadoPago()); // Corrigido aqui

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody User userAtualizado, Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        Optional<User> userOpt = userRepository.findById(idUsuario);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.setNome(userAtualizado.getNome());
        user.setSobrenome(userAtualizado.getSobrenome());
        user.setCelular(userAtualizado.getCelular());
        user.setCodigoMercadoPago(userAtualizado.getCodigoMercadoPago()); // Corrigido aqui

        userRepository.save(user);

        // Aqui convertemos para o DTO que não expõe a senha
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNome(user.getNome());
        response.setSobrenome(user.getSobrenome());
        response.setEmail(user.getEmail());
        response.setCelular(user.getCelular());
        response.setCpf(user.getCpf());
        response.setCodigoMercadoPago(user.getCodigoMercadoPago()); // Corrigido aqui também

        return ResponseEntity.ok(response);
    }
}
