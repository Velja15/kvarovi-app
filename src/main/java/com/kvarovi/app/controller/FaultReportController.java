package com.kvarovi.app.controller;

import com.kvarovi.app.dto.request.CreateFaultReportRequest;
import com.kvarovi.app.dto.request.UpdateStatusRequest;
import com.kvarovi.app.dto.response.FaultReportResponse;
import com.kvarovi.app.dto.response.StatusHistoryResponse;
import com.kvarovi.app.entity.enums.ReportStatus;
import com.kvarovi.app.service.FaultReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class FaultReportController {

    private final FaultReportService faultReportService;

    public FaultReportController(FaultReportService faultReportService) {
        this.faultReportService = faultReportService;
    }

    @PostMapping
    @PreAuthorize("hasRole('STANAR')")
    public ResponseEntity<FaultReportResponse> create(
            Authentication auth,
            @Valid @RequestBody CreateFaultReportRequest request) {
        return ResponseEntity.ok(faultReportService.createReport(auth.getName(), request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STANAR')")
    public ResponseEntity<List<FaultReportResponse>> myReports(Authentication auth) {
        return ResponseEntity.ok(faultReportService.getReportsForUser(auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FaultReportResponse>> allReports(
            Authentication auth,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(
                faultReportService.getReportsForAdmin(auth.getName(), status, categoryId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FaultReportResponse> updateStatus(
            @PathVariable Long id,
            Authentication auth,
            @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(
                faultReportService.updateStatus(id, auth.getName(), request));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StatusHistoryResponse>> history(@PathVariable Long id) {
        return ResponseEntity.ok(faultReportService.getHistory(id));
    }
}