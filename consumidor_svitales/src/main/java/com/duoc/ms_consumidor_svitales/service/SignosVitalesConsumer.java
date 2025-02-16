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
    
    @Autowired
    private VitalSignsService vitalSignsService;
    
    @Autowired
    private VitalSignsValidator validator;
    
    @Autowired
    private AlertService alertService;
    
    @KafkaListener(topics = "signs-vitales", groupId = "grupo-signos-vitales")
    public void consumir(String mensaje) {
        try {
            SignosVitalesDTO signosVitales = objectMapper.readValue(mensaje, SignosVitalesDTO.class);
            logger.info("Signos vitales recibidos - Paciente: {}", signosVitales.getPatientId());
            
            // Analizar los signos vitales
            analizarSignosVitales(signosVitales);
            
            // Verificar signos vitales y generar alertas si es necesario
            checkVitalSigns(signosVitales);
            
            // Enviar al backend
            vitalSignsService.sendVitalSigns(signosVitales);
            
        } catch (Exception e) {
            logger.error("Error al procesar mensaje de signos vitales: {}", e.getMessage());
        }
    }
    
    private void checkVitalSigns(SignosVitalesDTO signs) {
        if (!validator.isHeartRateNormal(signs.getHeartRate())) {
            String severity = validator.getAlertSeverity(signs.getHeartRate(), 60.0, 100.0);
            alertService.createAlert(
                signs.getPatientId(),
                "ABNORMAL_HEART_RATE",
                "Frecuencia cardíaca fuera de rango normal: " + signs.getHeartRate(),
                severity
            );
        }

        if (!validator.isBloodPressureNormal(
                signs.getBloodPressureSystolic(),
                signs.getBloodPressureDiastolic())) {
            alertService.createAlert(
                signs.getPatientId(),
                "ABNORMAL_BLOOD_PRESSURE",
                String.format("Presión arterial anormal: %f/%f",
                    signs.getBloodPressureSystolic(),
                    signs.getBloodPressureDiastolic()),
                "HIGH"
            );
        }

        if (!validator.isTemperatureNormal(signs.getBodyTemperature())) {
            String severity = validator.getAlertSeverity(signs.getBodyTemperature(), 36.5, 37.5);
            alertService.createAlert(
                signs.getPatientId(),
                "ABNORMAL_TEMPERATURE",
                "Temperatura corporal fuera de rango: " + signs.getBodyTemperature(),
                severity
            );
        }

        if (!validator.isOxygenSaturationNormal(signs.getOxygenSaturation())) {
            alertService.createAlert(
                signs.getPatientId(),
                "LOW_OXYGEN_SATURATION",
                "Saturación de oxígeno baja: " + signs.getOxygenSaturation(),
                "HIGH"
            );
        }
    }
    
    private void analizarSignosVitales(SignosVitalesDTO signosVitales) {
        logger.info("=== Análisis de Signos Vitales ===");
        logger.info("Paciente ID: {}", signosVitales.getPatientId());
        logger.info("Fecha Medición: {}", signosVitales.getTimestamp());
        logger.info("Temperatura: {}°C", signosVitales.getBodyTemperature());
        logger.info("Frecuencia Cardíaca: {} bpm", signosVitales.getHeartRate());
        logger.info("Presión Arterial: {}/{} mmHg", 
            signosVitales.getBloodPressureSystolic(), 
            signosVitales.getBloodPressureDiastolic());
        logger.info("Saturación de Oxígeno: {}%", signosVitales.getOxygenSaturation());
        logger.info("================================");
    }
}