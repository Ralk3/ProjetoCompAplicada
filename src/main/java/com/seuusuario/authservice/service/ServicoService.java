package com.seuusuario.authservice.service;

import com.seuusuario.authservice.entity.Servico;
import com.seuusuario.authservice.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico criar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public boolean deletarSeProprio(Long id, Long idUsuario) {
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        if (servicoOpt.isPresent() && servicoOpt.get().getIdUsuario().equals(idUsuario)) {
            servicoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Servico atualizarSeProprio(Long id, Servico servicoAtualizado, Long idUsuario) {
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        if (servicoOpt.isPresent()) {
            Servico servico = servicoOpt.get();
            if (servico.getIdUsuario().equals(idUsuario)) {
                servico.setNome(servicoAtualizado.getNome());
                servico.setDescricao(servicoAtualizado.getDescricao());
                servico.setPreco(servicoAtualizado.getPreco());
                servico.setAtivo(servicoAtualizado.getAtivo());
                return servicoRepository.save(servico);
            }
        }
        return null;
    }

    public List<Servico> listarPorUsuario(Long idUsuario) {
        return servicoRepository.findByIdUsuario(idUsuario);
    }

    // 🔥 Novo método: listar com paginação e filtro de nome
    public Page<Servico> listarComFiltroEPaginacao(String termo, Pageable pageable) {
        if (termo != null && !termo.isEmpty()) {
            return servicoRepository.findByNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(termo, termo, pageable);
        }
        return servicoRepository.findAll(pageable);
    }
}
