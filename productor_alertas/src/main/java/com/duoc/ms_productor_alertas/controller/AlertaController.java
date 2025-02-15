package com.duoc.ms_productor_alertas.controller;

import com.duoc.ms_productor_alertas.dto.AlertDTO;
import com.duoc.ms_productor_alertas.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping
    public ResponseEntity<String> enviarAlerta(@RequestBody AlertDTO alerta) {
        try {
            // Asignar valores por defecto si no vienen
            if (alerta.getTimestamp() == null) {
                alerta.setTimestamp(LocalDateTime.now());
            }
            if (alerta.getIsActive() == null) {
                alerta.setIsActive(true);
            }

            alertaService.enviarAlerta(alerta);
            return ResponseEntity.ok("Alerta enviada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al enviar alerta: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> enviarAlertaPrueba() {
        try {
            AlertDTO alerta = new AlertDTO();
            alerta.setId(1L);
            alerta.setPatientId(1001L);
            alerta.setAlertType("TEMPERATURE_HIGH");
            alerta.setSeverity("HIGH");
            alerta.setDescription("Temperatura corporal elevada");
            alerta.setTimestamp(LocalDateTime.now());
            alerta.setIsActive(true);

            alertaService.enviarAlerta(alerta);
            return ResponseEntity.ok("Alerta de prueba enviada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al enviar alerta de prueba: " + e.getMessage());
        }
    }
}