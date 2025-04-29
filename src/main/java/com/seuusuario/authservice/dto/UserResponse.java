package com.seuusuario.authservice.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String celular;
    private String cpf;
    private String codigoMercadoPago; // Corrigido aqui
}
