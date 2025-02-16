package com.duoc.ms_generador_svitales.service;

import com.duoc.ms_generador_svitales.dto.SignosVitalesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SignosVitalesGeneratorService {
    
    private static final Logger logger = LoggerFactory.getLogger(SignosVitalesGeneratorService.class);
    private static final String PRODUCER_URL = "http://20.62.11.94:8091/api/signos-vitales";
    private final Random random = new Random();
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Lista de IDs de pacientes para simular
    private final Long[] patientIds = {23L, 1L, 2L, 7L, 4L};
    
    @Scheduled(fixedDelay = 1000) // Espera 1 segundo después de que termine la ejecución anterior
    public void generateAndSendVitalSigns() {
        logger.info("Iniciando nueva generación de signos vitales...");
        
        for (Long patientId : patientIds) {
            try {
                SignosVitalesDTO vitalSigns = generateVitalSigns(patientId);
                
                // Enviar y esperar respuesta
                ResponseEntity<String> response = restTemplate.postForEntity(PRODUCER_URL, vitalSigns, String.class);
                
                // Solo logear éxito si el status es 200 OK
                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Generados y enviados signos vitales para paciente {}: HR={}, BP={}/{}, Temp={}, O2={}",
                        patientId,
                        vitalSigns.getHeartRate(),
                        vitalSigns.getBloodPressureSystolic(),
                        vitalSigns.getBloodPressureDiastolic(),
                        vitalSigns.getBodyTemperature(),
                        vitalSigns.getOxygenSaturation());
                } else {
                    logger.warn("Respuesta no exitosa al enviar signos vitales para paciente {}: {}", 
                        patientId, response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error al procesar paciente {}: {}", patientId, e.getMessage());
                // Continuamos con el siguiente paciente a pesar del error
            }
        }
        
        logger.info("Finalizada la generación de signos vitales. Esperando 1 segundo antes de la siguiente ejecución.");
    }
    
    private SignosVitalesDTO generateVitalSigns(Long patientId) {
        // Probabilidad del 3% de generar valores anormales
        boolean generateAbnormal = random.nextDouble() < 0.03;
        
        SignosVitalesDTO dto = new SignosVitalesDTO();
        dto.setPatientId(patientId);
        dto.setTimestamp(LocalDateTime.now());
        
        // Frecuencia cardíaca: Normal(60-100), Anormal(40-140)
        dto.setHeartRate(generateAbnormal ? 
            randomInRange(40.0, 140.0) : 
            randomInRange(60.0, 100.0));
        
        // Presión sistólica: Normal(90-140), Anormal(80-180)
        dto.setBloodPressureSystolic(generateAbnormal ? 
            randomInRange(80.0, 180.0) : 
            randomInRange(90.0, 140.0));
        
        // Presión diastólica: Normal(60-90), Anormal(50-120)
        dto.setBloodPressureDiastolic(generateAbnormal ? 
            randomInRange(50.0, 120.0) : 
            randomInRange(60.0, 90.0));
        
        // Temperatura: Normal(36.5-37.5), Anormal(36-39)
        dto.setBodyTemperature(generateAbnormal ? 
            randomInRange(36.0, 39.0) : 
            randomInRange(36.5, 37.5));
        
        // Saturación de oxígeno: Normal(>95), Anormal(85-100)
        dto.setOxygenSaturation(generateAbnormal ? 
            randomInRange(85.0, 100.0) : 
            randomInRange(95.0, 100.0));
        
        return dto;
    }
    
    private Double randomInRange(Double min, Double max) {
        return Math.round((min + (random.nextDouble() * (max - min))) * 10.0) / 10.0;
    }
}