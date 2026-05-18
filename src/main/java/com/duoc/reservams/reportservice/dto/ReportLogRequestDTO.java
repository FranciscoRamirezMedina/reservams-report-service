package com.duoc.reservams.reportservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO para registrar un reporte generado
@Data
public class ReportLogRequestDTO {

    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String reportType;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long generatedByUserId;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;
}