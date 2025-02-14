package com.duoc.ms_consumidor_alertas.service;

import com.duoc.ms_consumidor_alertas.dto.AlertaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AlertaConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertaConsumer.class);
    private final ObjectMapper objectMapper;
    
    public AlertaConsumer() {
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
            logger.info("=== Procesando Alerta ===");
            logger.info("Partition: {}, Offset: {}", partition, offset);
            logger.info("ID de Alerta: {}", alerta.getAlertaId());
            logger.info("ID de Paciente: {}", alerta.getPacienteId());
            logger.info("Tipo: {}", alerta.getTipo());
            logger.info("Severidad: {}", alerta.getSeveridad());
            logger.info("Descripción: {}", alerta.getDescripcion());
            logger.info("Valor Medido: {}", alerta.getValorMedido());
            logger.info("Valor de Referencia: {}", alerta.getValorReferencia());
            logger.info("Fecha de Alerta: {}", alerta.getFechaAlerta());
            logger.info("Atendida: {}", alerta.getAtendida());
            logger.info("=======================");
            
            procesarAlerta(alerta);
            
        } catch (Exception e) {
            logger.error("Error al procesar alerta - Partition: {}, Offset: {} - Error: {}", 
                partition, offset, e.getMessage());
        }
    }
    
    private void procesarAlerta(AlertaDTO alerta) {
        switch(alerta.getSeveridad().toUpperCase()) {
            case "CRITICA":
                logger.warn("¡ALERTA CRÍTICA! Se requiere atención inmediata para el paciente: {}", 
                    alerta.getPacienteId());
                break;
            case "ALTA":
                logger.warn("Alerta de alta prioridad para el paciente: {}", 
                    alerta.getPacienteId());
                break;
            case "MEDIA":
                logger.info("Alerta de prioridad media registrada para el paciente: {}", 
                    alerta.getPacienteId());
                break;
            case "BAJA":
                logger.info("Alerta de baja prioridad registrada para el paciente: {}", 
                    alerta.getPacienteId());
                break;
            default:
                logger.info("Alerta sin prioridad específica registrada para el paciente: {}", 
                    alerta.getPacienteId());
        }
    }
}