package com.seuusuario.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/teste")
    public String helloWorld() {
        return "Funcionando!";
    }
}
