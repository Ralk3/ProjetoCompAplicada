package com.seuusuario.authservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "imagens")
@Data
@NoArgsConstructor
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;
    private String secureUrl;
    private String format;
    private LocalDateTime createdAt;
    private Integer width;
    private Integer height;

    @JsonIgnore // 🔁 Impede serialização recursiva infinita
    @OneToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;
}
