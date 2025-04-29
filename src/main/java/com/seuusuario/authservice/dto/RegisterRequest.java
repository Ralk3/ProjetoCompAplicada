package com.seuusuario.authservice.dto;

public class RegisterRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String celular;
    private String cpf;
    private String codigoPagseguro;
    private String senha;

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getCodigoPagseguro() {
        return codigoPagseguro;
    }
    public void setCodigoPagseguro(String codigoPagseguro) {
        this.codigoPagseguro = codigoPagseguro;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
