package com.duoc.reservams.reportservice.service;

import com.duoc.reservams.reportservice.dto.ReportLogRequestDTO;
import com.duoc.reservams.reportservice.dto.ReportLogResponseDTO;
import com.duoc.reservams.reportservice.dto.ReportSummaryResponseDTO;
import com.duoc.reservams.reportservice.model.ReportLog;
import com.duoc.reservams.reportservice.repository.ReportLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de reportes
@Service
public class ReportService {

    private final ReportLogRepository reportLogRepository;

    public ReportService(ReportLogRepository reportLogRepository) {
        this.reportLogRepository = reportLogRepository;
    }

    public List<ReportLogResponseDTO> findAllLogs() {
        return reportLogRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReportLogResponseDTO findById(Long id) {
        ReportLog reportLog = reportLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        return toResponseDTO(reportLog);
    }

    public List<ReportLogResponseDTO> findByReportType(String reportType) {
        return reportLogRepository.findByReportType(reportType)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ReportLogResponseDTO> findByGeneratedByUserId(Long userId) {
        return reportLogRepository.findByGeneratedByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReportLogResponseDTO createLog(ReportLogRequestDTO request) {
        ReportLog reportLog = new ReportLog();

        reportLog.setReportType(request.getReportType());
        reportLog.setGeneratedByUserId(request.getGeneratedByUserId());
        reportLog.setDescription(request.getDescription());
        reportLog.setGeneratedAt(LocalDateTime.now());

        ReportLog savedReport = reportLogRepository.save(reportLog);

        return toResponseDTO(savedReport);
    }

    public void delete(Long id) {
        if (!reportLogRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }

        reportLogRepository.deleteById(id);
    }

    public ReportSummaryResponseDTO generateReservationsByHotelReport(Long hotelId, Long generatedByUserId) {
        // en esta versión basica solo dejamos registro del reporte generado
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("RESERVATIONS_BY_HOTEL");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de reservas generado para el hotel ID: " + hotelId);

        createLog(request);

        return new ReportSummaryResponseDTO(
                "RESERVATIONS_BY_HOTEL",
                "Reporte de reservas generado para el hotel ID: " + hotelId
        );
    }

    public ReportSummaryResponseDTO generatePaymentsByDateReport(String date, Long generatedByUserId) {
        // guardamos en historial que se genero este reporte
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("PAYMENTS_BY_DATE");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de pagos generado para la fecha: " + date);

        createLog(request);

        return new ReportSummaryResponseDTO(
                "PAYMENTS_BY_DATE",
                "Reporte de pagos generado para la fecha: " + date
        );
    }

    public ReportSummaryResponseDTO generateHotelOccupancyReport(Long hotelId, Long generatedByUserId) {
        // este reporte representa ocupacion del hotel de forma basica
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType("HOTEL_OCCUPANCY");
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte de ocupación generado para el hotel ID: " + hotelId);

        createLog(request);

        return new ReportSummaryResponseDTO(
                "HOTEL_OCCUPANCY",
                "Reporte de ocupación generado para el hotel ID: " + hotelId
        );
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