package com.seuusuario.authservice.dto;

import lombok.Data;

@Data
public class UsuarioInfoResponse {
    private String nome;
    private String email;
    private String celular;

    public UsuarioInfoResponse(String nome, String email, String celular) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }
}
