package com.seuusuario.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Será o external_reference

    private String mercadoPagoId; // preference_id do Mercado Pago

    private String initPoint; // URL de pagamento

    private String title; // Nome do serviço

    private String description; // Descrição do serviço (pode ser opcional)

    private Double unitPrice; // Preço do serviço

    private String status; // pending, approved, cancelled...

    private LocalDateTime dateOfExpiration; // Expiração do pagamento

    private LocalDateTime createdAt; // Quando foi criado

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Column(nullable = false)
    private Long userId;

}
