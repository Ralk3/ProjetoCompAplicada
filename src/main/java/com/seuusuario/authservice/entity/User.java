package com.seuusuario.authservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String celular;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "codigo_pagseguro")
    private String codigoMercadoPago; // AQUI fica o token do Mercado Pago

    @Column(nullable = false)
    private String senha;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCodigoMercadoPago() { return codigoMercadoPago; }
    public void setCodigoMercadoPago(String codigoMercadoPago) { this.codigoMercadoPago = codigoMercadoPago; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
