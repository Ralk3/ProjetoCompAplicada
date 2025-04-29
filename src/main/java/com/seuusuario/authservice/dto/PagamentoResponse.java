package com.seuusuario.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoResponse {

    private Long id;

    private String mercadoPagoId;

    private String initPoint;

    private String title;

    private String description;

    private Double unitPrice;

    private String status;

    private String dateOfExpiration;

    private String createdAt;
}
