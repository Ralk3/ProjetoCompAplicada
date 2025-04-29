package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.entity.Servico;
import com.seuusuario.authservice.service.ServicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    // POST - Cadastrar serviço
    @PostMapping
    public ResponseEntity<Servico> criarServico(@RequestBody Servico servico, Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        servico.setIdUsuario(idUsuario);

        return ResponseEntity.ok(servicoService.criar(servico));
    }

    // GET - Listar serviços com filtro por nome e paginação
    @GetMapping
    public ResponseEntity<Page<Servico>> listarServicos(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Servico> servicos = servicoService.listarComFiltroEPaginacao(nome, pageable);
        return ResponseEntity.ok(servicos);
    }

    // GET - Buscar serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarServico(@PathVariable Long id) {
        Servico servico = servicoService.buscarPorId(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(servico);
    }

    // DELETE - Deletar serviço (apenas se for do próprio usuário)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id, Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        boolean deletado = servicoService.deletarSeProprio(id, idUsuario);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build(); // Forbidden
    }

    // PUT - Atualizar serviço (apenas se for do próprio usuário)
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado, Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        Servico servico = servicoService.atualizarSeProprio(id, servicoAtualizado, idUsuario);
        if (servico != null) {
            return ResponseEntity.ok(servico);
        }
        return ResponseEntity.status(403).build(); // Forbidden
    }

    // GET - Buscar serviços cadastrados pelo próprio usuário
    @GetMapping("/meus")
    public ResponseEntity<List<Servico>> listarMeusServicos(Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(servicoService.listarPorUsuario(idUsuario));
    }
}
