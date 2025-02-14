package com.duoc.ms_consumidor_svitales.service;

import com.duoc.ms_consumidor_svitales.dto.SignosVitalesDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VitalSignsService {
    private static final Logger logger = LoggerFactory.getLogger(VitalSignsService.class);
    private static final String API_URL = "http://172.210.177.28:8081/api/vital-signs";
    private final RestTemplate restTemplate;

    public VitalSignsService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendVitalSigns(SignosVitalesDTO vitalSigns) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SignosVitalesDTO> request = new HttpEntity<>(vitalSigns, headers);
            
            logger.info("Enviando datos al endpoint: {}", API_URL);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Datos enviados exitosamente. Respuesta: {}", response.getBody());
            } else {
                logger.error("Error al enviar datos. Código de estado: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error al enviar los signos vitales: ", e);
        }
    }
}