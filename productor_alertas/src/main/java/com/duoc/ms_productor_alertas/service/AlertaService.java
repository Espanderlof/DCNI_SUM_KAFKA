package com.duoc.ms_productor_alertas.service;

import com.duoc.ms_productor_alertas.dto.AlertDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlertaService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertaService.class);
    private static final String TOPIC = "alertas";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public void enviarAlerta(AlertDTO alerta) {
        try {
            String mensaje = objectMapper.writeValueAsString(alerta);
            kafkaTemplate.send(TOPIC, alerta.getPatientId().toString(), mensaje)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Alerta enviada correctamente. ID: {}, Paciente: {}", 
                            alerta.getId(), alerta.getPatientId());
                    } else {
                        logger.error("Error al enviar alerta: {}", ex.getMessage());
                    }
                });
        } catch (Exception e) {
            logger.error("Error al serializar alerta: {}", e.getMessage());
            throw new RuntimeException("Error al enviar alerta", e);
        }
    }
}