package com.seuusuario.authservice.enums;

import lombok.Getter;

@Getter
public enum CategoriaServico {
    AULAS_E_CURSOS("Aulas e Cursos"),
    BABA_E_CUIDADOS_PESSOAIS("Babá e Cuidados Pessoais"),
    BELEZA_E_BEM_ESTAR("Beleza e Bem-estar"),
    CONSULTORIA_E_PROFISSIONAIS_LIBERAIS("Consultoria e Profissionais Liberais"),
    CULINARIA_E_CONFEITARIA("Culinária e Confeitaria"),
    EVENTOS_E_FESTAS("Eventos e Festas"),
    LOGISTICA_E_DISTRIBUICAO("Logística e Distribuição"),
    MARKETING_E_DESIGN("Marketing e Design"),
    MONTAGEM_E_INSTALACAO("Montagem e Instalação"),
    PET_SERVICES("Pet Services"),
    REPAROS_E_MANUTENCAO("Reparos e Manutenção"),
    SAUDE_E_BELEZA("Saúde e Beleza"),
    SERVICOS_DOMESTICOS("Serviços Domésticos"),
    TECNOLOGIA_E_INFORMATICA("Tecnologia e Informática"),
    TRANSPORTE_E_MUDANCAS("Transporte e Mudanças"),
    TURISMO_E_LAZER("Turismo e Lazer"),
    OUTROS_SERVICOS("Outros Serviços");

    private final String label;

    CategoriaServico(String label) {
        this.label = label;
    }
}
