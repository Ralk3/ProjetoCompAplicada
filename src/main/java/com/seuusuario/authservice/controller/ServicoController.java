package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.dto.CreateServicoRequest;
import com.seuusuario.authservice.entity.Imagem;
import com.seuusuario.authservice.entity.Servico;
import com.seuusuario.authservice.service.CloudinaryService;
import com.seuusuario.authservice.service.ServicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final CloudinaryService cloudinaryService; // ✅ Injeção do serviço

    public ServicoController(ServicoService servicoService, CloudinaryService cloudinaryService) {
        this.servicoService = servicoService;
        this.cloudinaryService = cloudinaryService;
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
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
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
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado,
            Authentication authentication) {
        Long idUsuario = Long.parseLong(authentication.getName());
        Servico servico = servicoService.atualizarSeProprio(id, servicoAtualizado, idUsuario);
        if (servico != null) {
            return ResponseEntity.ok(servico);
        }
        return ResponseEntity.status(403).build(); // Forbidden
    }




    @GetMapping("/meus")
public ResponseEntity<Page<Servico>> listarMeusServicos(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String direction
) {
    Long idUsuario = Long.parseLong(authentication.getName());

    Sort sortOrder;
    try {
        sortOrder = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
    } catch (Exception e) {
        sortOrder = Sort.by(Sort.Direction.DESC, "id");
    }

    Pageable pageable = PageRequest.of(page, size, sortOrder);
    return ResponseEntity.ok(servicoService.listarPorUsuario(idUsuario, pageable));
}


    

    // POST - Cadastrar serviço com imagem
    @PostMapping("/com-imagem")
    public ResponseEntity<?> criarServicoComImagem(@ModelAttribute CreateServicoRequest dto, Authentication auth) {
        try {
            Long idUsuario = Long.parseLong(auth.getName());

            Map<String, Object> cloudinary = cloudinaryService.uploadImagem(dto.getImagem());

            Servico servico = new Servico();
            servico.setNome(dto.getNome());
            servico.setDescricao(dto.getDescricao());
            servico.setPreco(dto.getPreco());
            servico.setAtivo(dto.getAtivo());
            servico.setIdUsuario(idUsuario);
            servico.setCep(dto.getCep());
            servico.setBairro(dto.getBairro());
            servico.setCidade(dto.getCidade());
            servico.setUf(dto.getUf());
            servico.setDdd(dto.getDdd());
            servico.setCodIbge(dto.getCodIbge());
            servico.setCategoria(dto.getCategoria());
            servico.setDataHora(java.time.LocalDateTime.now());

            Imagem imagem = new Imagem();
            imagem.setPublicId((String) cloudinary.get("public_id"));
            imagem.setSecureUrl((String) cloudinary.get("secure_url"));
            imagem.setFormat((String) cloudinary.get("format"));
            imagem.setWidth((Integer) cloudinary.get("width"));
            imagem.setHeight((Integer) cloudinary.get("height"));
            imagem.setCreatedAt(java.time.LocalDateTime.now());
            imagem.setServico(servico);

            servico.setImagem(imagem);

            return ResponseEntity.ok(servicoService.criar(servico));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar serviço com imagem: " + e.getMessage());
        }
    }

}
