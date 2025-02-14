package com.duoc.ms_productor_svitales.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignosVitalesDTO {
    private Long patientId;
    private Double heartRate;
    private Double bloodPressureSystolic;
    private Double bloodPressureDiastolic;
    private Double bodyTemperature;
    private Double oxygenSaturation;
    private LocalDateTime timestamp;
}