package com.duoc.ms_productor_svitales.service;

import com.duoc.ms_productor_svitales.dto.SignosVitalesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@EnableScheduling
public class SignosVitalesGeneratorService {
    
    private static final Logger logger = LoggerFactory.getLogger(SignosVitalesGeneratorService.class);
    private final Random random = new Random();
    
    @Autowired
    private SignosVitalesService signosVitalesService;
    
    // Lista de IDs de pacientes para simular
    private final Long[] patientIds = {23L, 1L, 2L, 7L, 4L};
    
    @Scheduled(fixedRate = 1000) // Ejecutar cada 1 segundo
    public void generateAndSendVitalSigns() {
        for (Long patientId : patientIds) {
            SignosVitalesDTO vitalSigns = generateVitalSigns(patientId);
            signosVitalesService.enviarSignosVitales(vitalSigns);
            logger.info("Generados signos vitales para paciente {}: HR={}, BP={}/{}, Temp={}, O2={}",
                patientId,
                vitalSigns.getHeartRate(),
                vitalSigns.getBloodPressureSystolic(),
                vitalSigns.getBloodPressureDiastolic(),
                vitalSigns.getBodyTemperature(),
                vitalSigns.getOxygenSaturation());
        }
    }
    
    private SignosVitalesDTO generateVitalSigns(Long patientId) {
        // Probabilidad del 1% de generar valores anormales
        boolean generateAbnormal = random.nextDouble() < 0.01;
        
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
        // Redondear a 1 decimal
        return Math.round((min + (random.nextDouble() * (max - min))) * 10.0) / 10.0;
    }
}