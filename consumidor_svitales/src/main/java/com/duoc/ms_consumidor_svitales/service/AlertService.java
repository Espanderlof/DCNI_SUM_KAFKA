package com.duoc.ms_consumidor_svitales.service;

import com.duoc.ms_consumidor_svitales.dto.AlertDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

@Service
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    private static final String API_URL = "http://20.62.11.94:8092/api/alertas";
    private final RestTemplate restTemplate;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 1000; // 1 segundo

    public AlertService() {
        this.restTemplate = new RestTemplate();
    }

    public void createAlert(Long patientId, String alertType, String description, String severity) {
        AlertDTO alert = new AlertDTO();
        alert.setPatientId(patientId);
        alert.setAlertType(alertType);
        alert.setDescription(description);
        alert.setSeverity(severity);
        alert.setTimestamp(LocalDateTime.now());
        alert.setIsActive(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlertDTO> request = new HttpEntity<>(alert, headers);

        int attempts = 0;
        boolean success = false;

        while (!success && attempts < MAX_RETRIES) {
            try {
                logger.info("Intento {} de enviar alerta al endpoint: {}", attempts + 1, API_URL);
                ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
                
                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Alerta enviada exitosamente. Respuesta: {}", response.getBody());
                    success = true;
                } else {
                    logger.error("Error al enviar alerta. Código de estado: {}", response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error al enviar la alerta (intento {}): {}", attempts + 1, e.getMessage());
                if (attempts < MAX_RETRIES - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            attempts++;
        }

        if (!success) {
            logger.error("No se pudo enviar la alerta después de {} intentos", MAX_RETRIES);
        }
    }
}