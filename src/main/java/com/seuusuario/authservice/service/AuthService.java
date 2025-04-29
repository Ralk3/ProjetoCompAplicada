package com.seuusuario.authservice.service;

import com.seuusuario.authservice.dto.LoginRequest;
import com.seuusuario.authservice.dto.RegisterRequest;
import com.seuusuario.authservice.entity.User;
import com.seuusuario.authservice.repository.UserRepository;
import com.seuusuario.authservice.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Usuário já cadastrado com este e-mail.");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setSobrenome(request.getSobrenome());
        user.setEmail(request.getEmail());
        user.setCelular(request.getCelular());
        user.setCpf(request.getCpf());
        user.setCodigoMercadoPago(request.getCodigoPagseguro()); // <-- Corrigido aqui
        user.setSenha(passwordEncoder.encode(request.getSenha())); // Criptografa a senha

        userRepository.save(user);

        return "Usuário registrado com sucesso.";
    }

    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {
            throw new RuntimeException("Senha inválida.");
        }

        return jwtService.generateToken(user); // Aqui gera o token JWT
    }
}
