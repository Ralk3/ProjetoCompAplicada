package com.seuusuario.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePagamentoRequest {

    private String title; // Título do serviço

    private String description; // Descrição do serviço (opcional)

    private Double unitPrice; // Valor do serviço

    private String dateOfExpiration; // Data de expiração (no formato ISO 8601)
}
