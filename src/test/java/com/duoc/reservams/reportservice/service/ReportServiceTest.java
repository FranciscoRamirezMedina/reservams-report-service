package com.duoc.reservams.reportservice.service;

import com.duoc.reservams.reportservice.dto.ReportLogRequestDTO;
import com.duoc.reservams.reportservice.dto.ReportLogResponseDTO;
import com.duoc.reservams.reportservice.dto.ReportSummaryResponseDTO;
import com.duoc.reservams.reportservice.model.ReportLog;
import com.duoc.reservams.reportservice.repository.ReportLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// pruebas unitarias para ReportService
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportLogRepository reportLogRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void findAllLogs_shouldReturnReportLogs() {
        // Given
        when(reportLogRepository.findAll()).thenReturn(List.of(
                buildReportLog(1L, "RESERVATIONS_BY_HOTEL", 1L),
                buildReportLog(2L, "PAYMENTS_BY_DATE", 2L)
        ));

        // When
        List<ReportLogResponseDTO> response = reportService.findAllLogs();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("RESERVATIONS_BY_HOTEL", response.get(0).getReportType());

        verify(reportLogRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnReportLog_whenExists() {
        // Given
        ReportLog reportLog = buildReportLog(1L, "RESERVATIONS_BY_HOTEL", 1L);

        when(reportLogRepository.findById(1L)).thenReturn(Optional.of(reportLog));

        // When
        ReportLogResponseDTO response = reportService.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("RESERVATIONS_BY_HOTEL", response.getReportType());
        assertEquals(1L, response.getGeneratedByUserId());
        assertNotNull(response.getGeneratedAt());

        verify(reportLogRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenReportLogNotFound() {
        // Given
        when(reportLogRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reportService.findById(99L)
        );

        // Then
        assertEquals("Reporte no encontrado", exception.getMessage());

        verify(reportLogRepository, times(1)).findById(99L);
    }

    @Test
    void findByReportType_shouldReturnReportLogs() {
        // Given
        when(reportLogRepository.findByReportType("PAYMENTS_BY_DATE")).thenReturn(List.of(
                buildReportLog(1L, "PAYMENTS_BY_DATE", 1L),
                buildReportLog(2L, "PAYMENTS_BY_DATE", 2L)
        ));

        // When
        List<ReportLogResponseDTO> response = reportService.findByReportType("PAYMENTS_BY_DATE");

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("PAYMENTS_BY_DATE", response.get(0).getReportType());

        verify(reportLogRepository, times(1)).findByReportType("PAYMENTS_BY_DATE");
    }

    @Test
    void findByGeneratedByUserId_shouldReturnReportLogs() {
        // Given
        when(reportLogRepository.findByGeneratedByUserId(1L)).thenReturn(List.of(
                buildReportLog(1L, "RESERVATIONS_BY_HOTEL", 1L),
                buildReportLog(2L, "HOTEL_OCCUPANCY", 1L)
        ));

        // When
        List<ReportLogResponseDTO> response = reportService.findByGeneratedByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getGeneratedByUserId());

        verify(reportLogRepository, times(1)).findByGeneratedByUserId(1L);
    }

    @Test
    void createLog_shouldCreateReportLog() {
        // Given
        ReportLogRequestDTO request = buildReportLogRequest("PAYMENTS_BY_DATE", 1L);

        when(reportLogRepository.save(any(ReportLog.class))).thenAnswer(invocation -> {
            ReportLog reportLog = invocation.getArgument(0);
            reportLog.setId(1L);
            return reportLog;
        });

        // When
        ReportLogResponseDTO response = reportService.createLog(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PAYMENTS_BY_DATE", response.getReportType());
        assertEquals(1L, response.getGeneratedByUserId());
        assertEquals("Reporte generado correctamente", response.getDescription());
        assertNotNull(response.getGeneratedAt());

        verify(reportLogRepository, times(1)).save(any(ReportLog.class));
    }

    @Test
    void delete_shouldDeleteReportLog_whenExists() {
        // Given
        when(reportLogRepository.existsById(1L)).thenReturn(true);

        // When
        reportService.delete(1L);

        // Then
        verify(reportLogRepository, times(1)).existsById(1L);
        verify(reportLogRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenReportLogNotFound() {
        // Given
        when(reportLogRepository.existsById(99L)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reportService.delete(99L)
        );

        // Then
        assertEquals("Reporte no encontrado", exception.getMessage());

        verify(reportLogRepository, times(1)).existsById(99L);
        verify(reportLogRepository, never()).deleteById(anyLong());
    }

    @Test
    void generateReservationsByHotelReport_shouldCreateLogAndReturnSummary() {
        // Given
        when(reportLogRepository.save(any(ReportLog.class))).thenAnswer(invocation -> {
            ReportLog reportLog = invocation.getArgument(0);
            reportLog.setId(1L);
            return reportLog;
        });

        // When
        ReportSummaryResponseDTO response = reportService.generateReservationsByHotelReport(1L, 10L);

        // Then
        assertNotNull(response);
        assertEquals("RESERVATIONS_BY_HOTEL", response.getReportType());
        assertEquals("Reporte de reservas generado para el hotel ID: 1", response.getMessage());

        verify(reportLogRepository, times(1)).save(any(ReportLog.class));
    }

    @Test
    void generatePaymentsByDateReport_shouldCreateLogAndReturnSummary() {
        // Given
        when(reportLogRepository.save(any(ReportLog.class))).thenAnswer(invocation -> {
            ReportLog reportLog = invocation.getArgument(0);
            reportLog.setId(1L);
            return reportLog;
        });

        // When
        ReportSummaryResponseDTO response = reportService.generatePaymentsByDateReport("2026-06-24", 10L);

        // Then
        assertNotNull(response);
        assertEquals("PAYMENTS_BY_DATE", response.getReportType());
        assertEquals("Reporte de pagos generado para la fecha: 2026-06-24", response.getMessage());

        verify(reportLogRepository, times(1)).save(any(ReportLog.class));
    }

    @Test
    void generateHotelOccupancyReport_shouldCreateLogAndReturnSummary() {
        // Given
        when(reportLogRepository.save(any(ReportLog.class))).thenAnswer(invocation -> {
            ReportLog reportLog = invocation.getArgument(0);
            reportLog.setId(1L);
            return reportLog;
        });

        // When
        ReportSummaryResponseDTO response = reportService.generateHotelOccupancyReport(1L, 10L);

        // Then
        assertNotNull(response);
        assertEquals("HOTEL_OCCUPANCY", response.getReportType());
        assertEquals("Reporte de ocupación generado para el hotel ID: 1", response.getMessage());

        verify(reportLogRepository, times(1)).save(any(ReportLog.class));
    }

    private ReportLogRequestDTO buildReportLogRequest(String reportType, Long generatedByUserId) {
        ReportLogRequestDTO request = new ReportLogRequestDTO();
        request.setReportType(reportType);
        request.setGeneratedByUserId(generatedByUserId);
        request.setDescription("Reporte generado correctamente");
        return request;
    }

    private ReportLog buildReportLog(Long id, String reportType, Long generatedByUserId) {
        ReportLog reportLog = new ReportLog();
        reportLog.setId(id);
        reportLog.setReportType(reportType);
        reportLog.setGeneratedByUserId(generatedByUserId);
        reportLog.setDescription("Reporte generado correctamente");
        reportLog.setGeneratedAt(LocalDateTime.now());
        return reportLog;
    }
}