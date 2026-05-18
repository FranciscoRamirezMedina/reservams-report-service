package com.duoc.reservams.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// DTO para responder datos de reportes generados
@Data
@AllArgsConstructor
public class ReportLogResponseDTO {

    private Long id;
    private String reportType;
    private Long generatedByUserId;
    private String description;
    private LocalDateTime generatedAt;
}