package com.seuusuario.authservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateServicoRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;

    private String cep;
    private String bairro;
    private String cidade;
    private String uf;
    private String ddd;
    private String codIbge;
    private String categoria;

    private MultipartFile imagem;
}
