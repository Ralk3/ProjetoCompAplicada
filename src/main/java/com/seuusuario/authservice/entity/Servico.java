package com.seuusuario.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "servicos")
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

    // Campos adicionais
    private String cep;
    private String bairro;
    private String cidade;
    private String uf;
    private String ddd;
    
    @Column(name = "cod_ibge")
    private String codIbge;

    private String categoria;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    // Relacionamento com imagem (1:1)
    @OneToOne(mappedBy = "servico", cascade = CascadeType.ALL)
    private Imagem imagem;

    public boolean isAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
}
