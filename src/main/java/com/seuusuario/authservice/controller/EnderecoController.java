package com.seuusuario.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/cep")
public class EnderecoController {

    @GetMapping("/{cep}")
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        // Validação simples
        if (!cep.matches("\\d{8}")) {
            return ResponseEntity.badRequest().body("CEP inválido. Use apenas 8 dígitos numéricos.");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://viacep.com.br/ws/" + cep + "/json/";

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            // Se houver erro no ViaCEP
            if (response.getBody() != null && response.getBody().containsKey("erro")) {
                return ResponseEntity.status(404).body("CEP não encontrado.");
            }

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao consultar o CEP: " + e.getMessage());
        }
    }
}
