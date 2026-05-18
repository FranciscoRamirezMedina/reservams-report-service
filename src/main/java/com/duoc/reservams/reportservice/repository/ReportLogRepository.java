package com.duoc.reservams.reportservice.repository;

import com.duoc.reservams.reportservice.model.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository para trabajar con la tabla report_logs
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {

    // lista reportes según su tipo
    List<ReportLog> findByReportType(String reportType);

    // lista reportes generados por un usuario específico
    List<ReportLog> findByGeneratedByUserId(Long generatedByUserId);
}