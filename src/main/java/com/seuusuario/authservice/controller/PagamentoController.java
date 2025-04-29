package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.dto.CreatePagamentoRequest;
import com.seuusuario.authservice.dto.PagamentoResponse;
import com.seuusuario.authservice.entity.Pagamento;
import com.seuusuario.authservice.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    // 1. Criar pagamento
    @PostMapping
    public ResponseEntity<PagamentoResponse> criarPagamento(@RequestBody CreatePagamentoRequest request, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PagamentoResponse response = pagamentoService.criarPagamento(request, userId);
        return ResponseEntity.ok(response);
    }

    // 2. Listar pagamentos do usuário
    @GetMapping
    public ResponseEntity<Page<Pagamento>> listarPagamentos(Pageable pageable, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Page<Pagamento> pagamentos = pagamentoService.listarPagamentos(userId, pageable);
        return ResponseEntity.ok(pagamentos);
    }

    // 3. Buscar pagamento específico do usuário
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPagamentoPorId(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Optional<Pagamento> pagamento = pagamentoService.buscarPagamentoPorId(id, userId);

        if (pagamento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pagamento.get());
    }

    // 4. Deletar pagamento do usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPagamento(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        pagamentoService.deletarPagamento(id, userId);
        return ResponseEntity.ok("Pagamento deletado com sucesso.");
    }

    // 5. Atualizar status (usado pelo n8n ou outro sistema interno)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatusPagamento(@PathVariable Long id, @RequestParam String status) {
        pagamentoService.atualizarStatusPagamento(id, status);
        return ResponseEntity.ok("Status atualizado com sucesso.");
    }
}
