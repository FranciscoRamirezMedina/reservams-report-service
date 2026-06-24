package com.duoc.reservams.reportservice.controller;

import com.duoc.reservams.reportservice.dto.ReportLogRequestDTO;
import com.duoc.reservams.reportservice.dto.ReportLogResponseDTO;
import com.duoc.reservams.reportservice.dto.ReportSummaryResponseDTO;
import com.duoc.reservams.reportservice.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// pruebas unitarias para ReportController
@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    void findAllLogs_shouldReturnReportLogs() {
        // Given
        when(reportService.findAllLogs()).thenReturn(List.of(
                buildReportLogResponse(1L, "RESERVATIONS_BY_HOTEL", 10L),
                buildReportLogResponse(2L, "PAYMENTS_BY_DATE", 20L)
        ));

        // When
        ResponseEntity<List<ReportLogResponseDTO>> response = reportController.findAllLogs();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(reportService, times(1)).findAllLogs();
    }

    @Test
    void findById_shouldReturnReportLog() {
        // Given
        when(reportService.findById(1L)).thenReturn(
                buildReportLogResponse(1L, "RESERVATIONS_BY_HOTEL", 10L)
        );

        // When
        ResponseEntity<ReportLogResponseDTO> response = reportController.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("RESERVATIONS_BY_HOTEL", response.getBody().getReportType());

        verify(reportService, times(1)).findById(1L);
    }

    @Test
    void findByReportType_shouldReturnReportLogs() {
        // Given
        when(reportService.findByReportType("PAYMENTS_BY_DATE")).thenReturn(List.of(
                buildReportLogResponse(1L, "PAYMENTS_BY_DATE", 10L),
                buildReportLogResponse(2L, "PAYMENTS_BY_DATE", 20L)
        ));

        // When
        ResponseEntity<List<ReportLogResponseDTO>> response =
                reportController.findByReportType("PAYMENTS_BY_DATE");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("PAYMENTS_BY_DATE", response.getBody().get(0).getReportType());

        verify(reportService, times(1)).findByReportType("PAYMENTS_BY_DATE");
    }

    @Test
    void findByGeneratedByUserId_shouldReturnReportLogs() {
        // Given
        when(reportService.findByGeneratedByUserId(10L)).thenReturn(List.of(
                buildReportLogResponse(1L, "RESERVATIONS_BY_HOTEL", 10L),
                buildReportLogResponse(2L, "HOTEL_OCCUPANCY", 10L)
        ));

        // When
        ResponseEntity<List<ReportLogResponseDTO>> response =
                reportController.findByGeneratedByUserId(10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getGeneratedByUserId());

        verify(reportService, times(1)).findByGeneratedByUserId(10L);
    }

    @Test
    void createLog_shouldReturnCreatedReportLog() {
        // Given
        ReportLogRequestDTO request = buildReportLogRequest("PAYMENTS_BY_DATE", 10L);

        when(reportService.createLog(request)).thenReturn(
                buildReportLogResponse(1L, "PAYMENTS_BY_DATE", 10L)
        );

        // When
        ResponseEntity<ReportLogResponseDTO> response = reportController.createLog(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("PAYMENTS_BY_DATE", response.getBody().getReportType());

        verify(reportService, times(1)).createLog(request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // Given
        doNothing().when(reportService).delete(1L);

        // When
        ResponseEntity<Void> response = reportController.delete(1L);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(reportService, times(1)).delete(1L);
    }

    @Test
    void generateReservationsByHotelReport_shouldReturnSummary() {
        // Given
        when(reportService.generateReservationsByHotelReport(1L, 10L)).thenReturn(
                new ReportSummaryResponseDTO(
                        "RESERVATIONS_BY_HOTEL",
                        "Reporte de reservas generado para el hotel ID: 1"
                )
        );

        // When
        ResponseEntity<ReportSummaryResponseDTO> response =
                reportController.generateReservationsByHotelReport(1L, 10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("RESERVATIONS_BY_HOTEL", response.getBody().getReportType());
        assertEquals("Reporte de reservas generado para el hotel ID: 1", response.getBody().getMessage());

        verify(reportService, times(1)).generateReservationsByHotelReport(1L, 10L);
    }

    @Test
    void generatePaymentsByDateReport_shouldReturnSummary() {
        // Given
        when(reportService.generatePaymentsByDateReport("2026-06-24", 10L)).thenReturn(
                new ReportSummaryResponseDTO(
                        "PAYMENTS_BY_DATE",
                        "Reporte de pagos generado para la fecha: 2026-06-24"
                )
        );

        // When
        ResponseEntity<ReportSummaryResponseDTO> response =
                reportController.generatePaymentsByDateReport("2026-06-24", 10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("PAYMENTS_BY_DATE", response.getBody().getReportType());
        assertEquals("Reporte de pagos generado para la fecha: 2026-06-24", response.getBody().getMessage());

        verify(reportService, times(1)).generatePaymentsByDateReport("2026-06-24", 10L);
    }

    @Test
    void generateHotelOccupancyReport_shouldReturnSummary() {
        // Given
        when(reportService.generateHotelOccupancyReport(1L, 10L)).thenReturn(
                new ReportSummaryResponseDTO(
                        "HOTEL_OCCUPANCY",
                        "Reporte de ocupación generado para el hotel ID: 1"
                )
        );

        // When
        ResponseEntity<ReportSummaryResponseDTO> response =
                reportController.generateHotelOccupancyReport(1L, 10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("HOTEL_OCCUPANCY", response.getBody().getReportType());
        assertEquals("Reporte de ocupación generado para el hotel ID: 1", response.getBody().getMessage());

        verify(reportService, times(1)).generateHotelOccupancyReport(1L, 10L);
    }

    private ReportLogRequestDTO buildReportLogRequest(String reportType, Long generatedByUserId) {
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType(reportType);
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte generado correctamente");
        return request;
    }

    private ReportLogResponseDTO buildReportLogResponse(Long id, String reportType, Long generatedByUserId) {
        return new ReportLogResponseDTO(
                id,
                reportType,
                generatedByUserId,
                "Reporte generado correctamente",
                LocalDateTime.now()
        );
    }
}