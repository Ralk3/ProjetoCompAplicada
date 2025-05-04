package com.seuusuario.authservice.repository;

import com.seuusuario.authservice.entity.Pagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // Buscar pagamentos de um usuário específico
    Page<Pagamento> findAllByUserId(Long userId, Pageable pageable);


}
