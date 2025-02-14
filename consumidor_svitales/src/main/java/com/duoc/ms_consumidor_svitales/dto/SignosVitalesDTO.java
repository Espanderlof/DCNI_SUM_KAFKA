package com.duoc.ms_consumidor_svitales.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignosVitalesDTO {
    private String pacienteId;
    private Double temperatura;
    private Integer frecuenciaCardiaca;
    private Integer presionSistolica;
    private Integer presionDiastolica;
    private Integer saturacionOxigeno;
    private LocalDateTime fechaMedicion;
}