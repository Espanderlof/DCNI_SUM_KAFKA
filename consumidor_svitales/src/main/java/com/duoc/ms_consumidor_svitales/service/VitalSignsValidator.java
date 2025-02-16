package com.duoc.ms_consumidor_svitales.service;

import org.springframework.stereotype.Service;

@Service
public class VitalSignsValidator {
    public boolean isHeartRateNormal(Double heartRate) {
        return heartRate >= 60 && heartRate <= 100;
    }

    public boolean isBloodPressureNormal(Double systolic, Double diastolic) {
        return systolic >= 90 && systolic <= 140 && diastolic >= 60 && diastolic <= 90;
    }

    public boolean isTemperatureNormal(Double temperature) {
        return temperature >= 36.5 && temperature <= 37.5;
    }

    public boolean isOxygenSaturationNormal(Double saturation) {
        return saturation >= 95;
    }

    public String getAlertSeverity(Double value, Double min, Double max) {
        double minDeviation = (max - min) * 0.1; // 10% de desviación
        
        if (value < min - minDeviation || value > max + minDeviation) {
            return "HIGH";
        }
        return "MEDIUM";
    }
}