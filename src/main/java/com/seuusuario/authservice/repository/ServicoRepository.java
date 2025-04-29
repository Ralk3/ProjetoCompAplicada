package com.seuusuario.authservice.repository;

import com.seuusuario.authservice.entity.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByIdUsuario(Long idUsuario);

    // Novo: buscar por nome contendo texto (filtro) e paginando
    Page<Servico> findByNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String nome, String descricao, Pageable pageable);
    // Se quiser sem filtro (todos paginados)
    Page<Servico> findAll(Pageable pageable);
}
