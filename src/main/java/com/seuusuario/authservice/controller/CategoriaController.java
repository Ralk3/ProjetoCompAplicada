package com.seuusuario.authservice.controller;

import com.seuusuario.authservice.enums.CategoriaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorias-servico")
public class CategoriaController {

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> listarCategorias() {
        List<Map<String, String>> categorias = Arrays.stream(CategoriaServico.values())
                .map(categoria -> Map.of(
                        "id", categoria.name(),
                        "label", categoria.getLabel()
                ))
                .toList();

        return ResponseEntity.ok(categorias);
    }
}
