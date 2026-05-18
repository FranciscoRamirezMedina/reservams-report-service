package com.duoc.reservams.reportservice.controller;

import com.duoc.reservams.reportservice.dto.ReportLogRequestDTO;
import com.duoc.reservams.reportservice.dto.ReportLogResponseDTO;
import com.duoc.reservams.reportservice.dto.ReportSummaryResponseDTO;
import com.duoc.reservams.reportservice.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador REST para manejar reportes
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // lista el historial de reportes generados
    @GetMapping("/logs")
    public ResponseEntity<List<ReportLogResponseDTO>> findAllLogs() {
        return ResponseEntity.ok(reportService.findAllLogs());
    }

    // busca un registro de reporte por ID
    @GetMapping("/logs/{id}")
    public ResponseEntity<ReportLogResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    // lista reportes por tipo
    @GetMapping("/logs/type/{reportType}")
    public ResponseEntity<List<ReportLogResponseDTO>> findByReportType(@PathVariable String reportType) {
        return ResponseEntity.ok(reportService.findByReportType(reportType));
    }

    // lista reportes generados por un usuario
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<ReportLogResponseDTO>> findByGeneratedByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.findByGeneratedByUserId(userId));
    }

    // crea manualmente un registro de reporte
    @PostMapping("/logs")
    public ResponseEntity<ReportLogResponseDTO> createLog(@Valid @RequestBody ReportLogRequestDTO request) {
        return ResponseEntity.ok(reportService.createLog(request));
    }

    // elimina un registro de reporte
    @DeleteMapping("/logs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // genera reporte basico de reservas por hotel
    @PostMapping("/reservations-by-hotel/{hotelId}/user/{userId}")
    public ResponseEntity<ReportSummaryResponseDTO> generateReservationsByHotelReport(
            @PathVariable Long hotelId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(reportService.generateReservationsByHotelReport(hotelId, userId));
    }

    // genera reporte basico de pagos por fecha
    @PostMapping("/payments-by-date/{date}/user/{userId}")
    public ResponseEntity<ReportSummaryResponseDTO> generatePaymentsByDateReport(
            @PathVariable String date,
            @PathVariable Long userId) {

        return ResponseEntity.ok(reportService.generatePaymentsByDateReport(date, userId));
    }

    // genera reporte basico de ocupacion por hotel
    @PostMapping("/hotel-occupancy/{hotelId}/user/{userId}")
    public ResponseEntity<ReportSummaryResponseDTO> generateHotelOccupancyReport(
            @PathVariable Long hotelId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(reportService.generateHotelOccupancyReport(hotelId, userId));
    }
}