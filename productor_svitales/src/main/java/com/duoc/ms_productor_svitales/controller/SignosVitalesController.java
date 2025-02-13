package com.duoc.ms_productor_svitales.controller;

import com.duoc.ms_productor_svitales.dto.SignosVitalesDTO;
import com.duoc.ms_productor_svitales.service.SignosVitalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signos-vitales")
public class SignosVitalesController {

    @Autowired
    private SignosVitalesService signosVitalesService;

    @PostMapping
    public ResponseEntity<String> enviarSignosVitales(@RequestBody SignosVitalesDTO signosVitales) {
        try {
            signosVitalesService.enviarSignosVitales(signosVitales);
            return ResponseEntity.ok("Signos vitales enviados correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar signos vitales: " + e.getMessage());
        }
    }
}