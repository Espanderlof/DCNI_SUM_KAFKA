package com.duoc.ms_productor_svitales.service;

import com.duoc.ms_productor_svitales.dto.SignosVitalesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SignosVitalesService {
    
    private static final Logger logger = LoggerFactory.getLogger(SignosVitalesService.class);
    private static final String TOPIC = "signs-vitales";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public void enviarSignosVitales(SignosVitalesDTO signosVitales) {
        try {
            String mensaje = objectMapper.writeValueAsString(signosVitales);
            kafkaTemplate.send(TOPIC, signosVitales.getPacienteId(), mensaje);
            logger.info("Signos vitales enviados correctamente para el paciente: {}", signosVitales.getPacienteId());
        } catch (Exception e) {
            logger.error("Error al enviar signos vitales: {}", e.getMessage());
            throw new RuntimeException("Error al enviar signos vitales", e);
        }
    }
}