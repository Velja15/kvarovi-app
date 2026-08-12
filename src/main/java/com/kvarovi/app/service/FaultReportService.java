package com.kvarovi.app.service;

import com.kvarovi.app.dto.request.CreateFaultReportRequest;
import com.kvarovi.app.dto.request.UpdateStatusRequest;
import com.kvarovi.app.dto.response.FaultReportResponse;
import com.kvarovi.app.dto.response.StatusHistoryResponse;
import com.kvarovi.app.entity.*;
import com.kvarovi.app.entity.enums.Priority;
import com.kvarovi.app.entity.enums.ReportStatus;
import com.kvarovi.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaultReportService {

    private final FaultReportRepository faultReportRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public FaultReportService(FaultReportRepository faultReportRepository,
                              StatusHistoryRepository statusHistoryRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.faultReportRepository = faultReportRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public FaultReportResponse createReport(String username, CreateFaultReportRequest request) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategorija nije pronadjena"));

        FaultReport report = new FaultReport();
        report.setDescription(request.getDescription());
        report.setLocation(request.getLocation());
        report.setStatus(ReportStatus.PRIJAVLJENO);
        report.setPriority(Priority.SREDNJI);
        report.setCreatedAt(LocalDateTime.now());
        report.setReporter(reporter);
        report.setBuilding(reporter.getBuilding());
        report.setCategory(category);

        FaultReport saved = faultReportRepository.save(report);

        StatusHistory history = new StatusHistory();
        history.setReport(saved);
        history.setOldStatus(null);
        history.setNewStatus(ReportStatus.PRIJAVLJENO);
        history.setChangedBy(reporter);
        history.setChangedAt(LocalDateTime.now());
        statusHistoryRepository.save(history);

        return mapToResponse(saved);
    }

    @Transactional
    public FaultReportResponse updateStatus(Long reportId, String adminUsername,
                                            UpdateStatusRequest request) {
        FaultReport report = faultReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Prijava nije pronadjena"));

        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin nije pronadjen"));

        ReportStatus oldStatus = report.getStatus();

        report.setStatus(request.getNewStatus());
        if (request.getPriority() != null) {
            report.setPriority(request.getPriority());
        }
        faultReportRepository.save(report);

        StatusHistory history = new StatusHistory();
        history.setReport(report);
        history.setOldStatus(oldStatus);
        history.setNewStatus(request.getNewStatus());
        history.setChangedBy(admin);
        history.setChangedAt(LocalDateTime.now());
        statusHistoryRepository.save(history);

        return mapToResponse(report);
    }

    public List<FaultReportResponse> getReportsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen"));
        return faultReportRepository.findByReporterId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FaultReportResponse> getReportsForAdmin(String adminUsername,
                                                        ReportStatus status,
                                                        Long categoryId) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin nije pronadjen"));
        Long buildingId = admin.getBuilding().getId();

        List<FaultReport> reports;
        if (status != null) {
            reports = faultReportRepository.findByBuildingIdAndStatus(buildingId, status);
        } else if (categoryId != null) {
            reports = faultReportRepository.findByBuildingIdAndCategoryId(buildingId, categoryId);
        } else {
            reports = faultReportRepository.findByBuildingId(buildingId);
        }

        return reports.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<StatusHistoryResponse> getHistory(Long reportId) {
        return statusHistoryRepository.findByReportIdOrderByChangedAtAsc(reportId).stream()
                .map(h -> new StatusHistoryResponse(
                        h.getOldStatus(),
                        h.getNewStatus(),
                        h.getChangedBy() != null ? h.getChangedBy().getFullName() : "Sistem",
                        h.getChangedAt()
                ))
                .collect(Collectors.toList());
    }

    private FaultReportResponse mapToResponse(FaultReport report) {
        return new FaultReportResponse(
                report.getId(),
                report.getDescription(),
                report.getLocation(),
                report.getStatus(),
                report.getPriority(),
                report.getCreatedAt(),
                report.getReporter().getFullName(),
                report.getCategory().getName(),
                report.getBuilding().getName()
        );
    }
}