package com.controller;

import com.service.IaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    /**
     * IaController expone el endpoint REST que recibe prompts y delega:
     * - IaService recibe el prompt, añade esquema y consulta a la IA
     * - La IA devuelve la sentencia SQL
     * - IaService valida y ejecuta la SQL
     * - El controlador devuelve los resultados al cliente
     */
    @Autowired
    private IaService iaService;

    @PostMapping(value = "/prompt", produces = "application/json")
    public ResponseEntity<?> procesarPrompt(@RequestBody String prompt) {
        try {
            return iaService.procesarPrompt(prompt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el prompt: " + e.getMessage());
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
