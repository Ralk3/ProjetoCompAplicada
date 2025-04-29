package com.seuusuario.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicos") // <- Aqui
@Data
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private Boolean ativo;

    public boolean isAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
}
