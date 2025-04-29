package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.dto.UsuarioInfoResponse;
import com.seuusuario.authservice.entity.User;
import com.seuusuario.authservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInfoResponse> buscarUsuarioPorId(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> ResponseEntity.ok(new UsuarioInfoResponse(
                user.getNome(),
                user.getEmail(),
                user.getCelular() // Corrigido aqui
            )))
            .orElse(ResponseEntity.notFound().build());
    }
}
