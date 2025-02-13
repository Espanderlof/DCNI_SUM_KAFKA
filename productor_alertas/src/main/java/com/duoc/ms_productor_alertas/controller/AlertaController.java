package com.duoc.ms_productor_alertas.controller;

import com.duoc.ms_productor_alertas.dto.AlertaDTO;
import com.duoc.ms_productor_alertas.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping
    public ResponseEntity<String> enviarAlerta(@RequestBody AlertaDTO alerta) {
        try {
            // Asignar valores por defecto si no vienen
            if (alerta.getAlertaId() == null) {
                alerta.setAlertaId(UUID.randomUUID().toString());
            }
            if (alerta.getFechaAlerta() == null) {
                alerta.setFechaAlerta(LocalDateTime.now());
            }
            if (alerta.getAtendida() == null) {
                alerta.setAtendida(false);
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
            AlertaDTO alerta = new AlertaDTO();
            alerta.setAlertaId(UUID.randomUUID().toString());
            alerta.setPacienteId("TEST001");
            alerta.setTipo("TEMPERATURA_ALTA");
            alerta.setSeveridad("ALTA");
            alerta.setDescripcion("Temperatura corporal elevada");
            alerta.setValorMedido(39.5);
            alerta.setValorReferencia(37.5);
            alerta.setFechaAlerta(LocalDateTime.now());
            alerta.setAtendida(false);

            alertaService.enviarAlerta(alerta);
            return ResponseEntity.ok("Alerta de prueba enviada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error al enviar alerta de prueba: " + e.getMessage());
        }
    }
}