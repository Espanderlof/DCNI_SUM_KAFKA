package com.duoc.ms_consumidor_alertas.service;

import com.duoc.ms_consumidor_alertas.dto.AlertaDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    private static final String API_URL = "http://172.210.177.28:8081/api/alerts";
    private final RestTemplate restTemplate;

    public AlertService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendAlert(AlertaDTO alert) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AlertaDTO> request = new HttpEntity<>(alert, headers);
            
            logger.info("Enviando alerta al endpoint: {}", API_URL);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Alerta enviada exitosamente. Respuesta: {}", response.getBody());
            } else {
                logger.error("Error al enviar alerta. Código de estado: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error al enviar la alerta: ", e);
        }
    }
}