package com.duoc.reservams.reportservice.service;

import com.duoc.reservams.reportservice.dto.ReportLogRequestDTO;
import com.duoc.reservams.reportservice.dto.ReportLogResponseDTO;
import com.duoc.reservams.reportservice.dto.ReportSummaryResponseDTO;
import com.duoc.reservams.reportservice.model.ReportLog;
import com.duoc.reservams.reportservice.repository.ReportLogRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de reportes
@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ReportLogRepository reportLogRepository;

    public ReportService(ReportLogRepository reportLogRepository) {
        this.reportLogRepository = reportLogRepository;
    }

    public List<ReportLogResponseDTO> findAllLogs() {
        logger.info("Listando logs de reportes");

        return reportLogRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReportLogResponseDTO findById(Long id) {
        ReportLog reportLog = findReportLogOrThrow(id);

        return toResponseDTO(reportLog);
    }

    public List<ReportLogResponseDTO> findByReportType(String reportType) {
        logger.info("Listando reportes por tipo {}", reportType);

        return reportLogRepository.findByReportType(reportType)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ReportLogResponseDTO> findByGeneratedByUserId(Long userId) {
        logger.info("Listando reportes generados por usuario ID {}", userId);

        return reportLogRepository.findByGeneratedByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReportLogResponseDTO createLog(ReportLogRequestDTO request) {
        logger.info("Creando log de reporte tipo {} para usuario ID {}",
                request.getReportType(),
                request.getGeneratedByUserId());

        ReportLog reportLog = new ReportLog();

        reportLog.setReportType(request.getReportType());
        reportLog.setGeneratedByUserId(request.getGeneratedByUserId());
        reportLog.setDescription(request.getDescription());
        reportLog.setGeneratedAt(LocalDateTime.now());

        ReportLog savedReport = reportLogRepository.save(reportLog);

        logger.info("Log de reporte creado correctamente con ID {}", savedReport.getId());

        return toResponseDTO(savedReport);
    }

    public void delete(Long id) {
        logger.info("Iniciando eliminacion de reporte ID {}", id);

        if (!reportLogRepository.existsById(id)) {
            logger.warn("Reporte no encontrado con ID {}", id);
            throw new RuntimeException("Reporte no encontrado");
        }

        reportLogRepository.deleteById(id);

        logger.info("Reporte ID {} eliminado correctamente", id);
    }

    public ReportSummaryResponseDTO generateReservationsByHotelReport(Long hotelId, Long generatedByUserId) {
        logger.info("Generando reporte de reservas para hotel ID {} por usuario ID {}",
                hotelId,
                generatedByUserId);

        // en esta versión basica solo dejamos registro del reporte generado
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("RESERVATIONS_BY_HOTEL");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de reservas generado para el hotel ID: " + hotelId);

        createLog(request);

        logger.info("Reporte RESERVATIONS_BY_HOTEL generado correctamente para hotel ID {}", hotelId);

        return new ReportSummaryResponseDTO(
                "RESERVATIONS_BY_HOTEL",
                "Reporte de reservas generado para el hotel ID: " + hotelId
        );
    }

    public ReportSummaryResponseDTO generatePaymentsByDateReport(String date, Long generatedByUserId) {
        logger.info("Generando reporte de pagos para fecha {} por usuario ID {}",
                date,
                generatedByUserId);

        // guardamos en historial que se genero este reporte
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("PAYMENTS_BY_DATE");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de pagos generado para la fecha: " + date);

        createLog(request);

        logger.info("Reporte PAYMENTS_BY_DATE generado correctamente para fecha {}", date);

        return new ReportSummaryResponseDTO(
                "PAYMENTS_BY_DATE",
                "Reporte de pagos generado para la fecha: " + date
        );
    }

    public ReportSummaryResponseDTO generateHotelOccupancyReport(Long hotelId, Long generatedByUserId) {
        logger.info("Generando reporte de ocupacion para hotel ID {} por usuario ID {}",
                hotelId,
                generatedByUserId);

        // este reporte representa ocupacion del hotel de forma basica
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("HOTEL_OCCUPANCY");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de ocupación generado para el hotel ID: " + hotelId);

        createLog(request);

        logger.info("Reporte HOTEL_OCCUPANCY generado correctamente para hotel ID {}", hotelId);

        return new ReportSummaryResponseDTO(
                "HOTEL_OCCUPANCY",
                "Reporte de ocupación generado para el hotel ID: " + hotelId
        );
    }

    private ReportLog findReportLogOrThrow(Long id) {
        return reportLogRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Reporte no encontrado con ID {}", id);
                    return new RuntimeException("Reporte no encontrado");
                });
    }

    // convierte la entidad ReportLog a DTO de respuesta
    private ReportLogResponseDTO toResponseDTO(ReportLog reportLog) {
        return new ReportLogResponseDTO(
                reportLog.getId(),
                reportLog.getReportType(),
                reportLog.getGeneratedByUserId(),
                reportLog.getDescription(),
                reportLog.getGeneratedAt()
        );
    }
}