package com.duoc.ms_productor_alertas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertaDTO {
    private String alertaId;
    private String pacienteId;
    private String tipo;
    private String severidad;
    private String descripcion;
    private Double valorMedido;
    private Double valorReferencia;
    private LocalDateTime fechaAlerta;
    private Boolean atendida;
}