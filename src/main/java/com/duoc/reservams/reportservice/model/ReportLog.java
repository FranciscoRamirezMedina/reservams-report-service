package com.duoc.reservams.reportservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// esta clase representa un registro de reporte generado
@Entity
@Table(name = "report_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportLog {

    // ID principal del registro
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // tipo de reporte generado
    @Column(name = "report_type", nullable = false, length = 80)
    private String reportType;

    // ID logico del usuario que genero el reporte
    @Column(name = "generated_by_user_id", nullable = false)
    private Long generatedByUserId;

    // descripcion simple del reporte
    @Column(nullable = false, length = 255)
    private String description;

    // fecha en que se genero el reporte
    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();
}