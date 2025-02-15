package com.duoc.ms_consumidor_alertas.service;

import com.duoc.ms_consumidor_alertas.dto.AlertaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AlertaConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertaConsumer.class);
    private final ObjectMapper objectMapper;
    private final AlertService alertService;
    
    @Autowired
    public AlertaConsumer(AlertService alertService) {
        this.alertService = alertService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @KafkaListener(topics = "alertas", groupId = "grupo-alertas")
    public void consumir(
            @Payload String mensaje,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            AlertaDTO alerta = objectMapper.readValue(mensaje, AlertaDTO.class);
            logger.info("=== Alerta Recibida ===");
            logger.info("Partition: {}, Offset: {}", partition, offset);
            logger.info("ID: {}", alerta.getId());
            logger.info("Patient ID: {}", alerta.getPatientId());
            logger.info("Alert Type: {}", alerta.getAlertType());
            logger.info("Severity: {}", alerta.getSeverity());
            logger.info("Description: {}", alerta.getDescription());
            logger.info("Timestamp: {}", alerta.getTimestamp());
            logger.info("Is Active: {}", alerta.getIsActive());
            logger.info("=======================");
            
            // Enviar la alerta al endpoint
            alertService.sendAlert(alerta);
            
        } catch (Exception e) {
            logger.error("Error al procesar mensaje - Partition: {}, Offset: {} - Error: {}", 
                partition, offset, e.getMessage());
        }
    }
}