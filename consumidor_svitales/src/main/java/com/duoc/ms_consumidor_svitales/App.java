package com.duoc.ms_consumidor_svitales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("Microservicio kafka consumidor signos vitales iniciado correctamente...");
    }
}