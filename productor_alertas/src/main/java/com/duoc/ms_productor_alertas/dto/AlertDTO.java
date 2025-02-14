package com.duoc.ms_productor_alertas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertDTO {
    private Long id;
    private Long patientId;
    private String alertType;
    private String description;
    private String severity;
    private LocalDateTime timestamp;
    private Boolean isActive;
}