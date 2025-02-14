package com.duoc.ms_consumidor_svitales.service;

import com.duoc.ms_consumidor_svitales.dto.SignosVitalesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SignosVitalesConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(SignosVitalesConsumer.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @KafkaListener(topics = "signs-vitales", groupId = "grupo-signos-vitales")
    public void consumir(String mensaje) {
        try {
            SignosVitalesDTO signosVitales = objectMapper.readValue(mensaje, SignosVitalesDTO.class);
            logger.info("Signos vitales recibidos - Paciente: {}", signosVitales.getPacienteId());
            logger.info("Detalles: {}", signosVitales);
            
            // Aquí puedes agregar la lógica adicional que necesites
            analizarSignosVitales(signosVitales);
            
        } catch (Exception e) {
            logger.error("Error al procesar mensaje de signos vitales: {}", e.getMessage());
        }
    }
    
    private void analizarSignosVitales(SignosVitalesDTO signosVitales) {
        logger.info("=== Análisis de Signos Vitales ===");
        logger.info("Paciente ID: {}", signosVitales.getPacienteId());
        logger.info("Fecha Medición: {}", signosVitales.getFechaMedicion());
        logger.info("Temperatura: {}°C", signosVitales.getTemperatura());
        logger.info("Frecuencia Cardíaca: {} bpm", signosVitales.getFrecuenciaCardiaca());
        logger.info("Presión Arterial: {}/{} mmHg", 
            signosVitales.getPresionSistolica(), 
            signosVitales.getPresionDiastolica());
        logger.info("Saturación de Oxígeno: {}%", signosVitales.getSaturacionOxigeno());
        logger.info("================================");
    }
}