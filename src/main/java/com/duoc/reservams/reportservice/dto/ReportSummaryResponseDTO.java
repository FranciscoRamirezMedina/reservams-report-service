package com.duoc.reservams.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO simple para responder un resumen de reporte (simula una respuesta de reporte)
@Data
@AllArgsConstructor
public class ReportSummaryResponseDTO {

    private String reportType;
    private String message;
}