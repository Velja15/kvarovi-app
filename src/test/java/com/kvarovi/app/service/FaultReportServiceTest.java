package com.kvarovi.app.service;

import com.kvarovi.app.dto.request.UpdateStatusRequest;
import com.kvarovi.app.dto.response.FaultReportResponse;
import com.kvarovi.app.entity.*;
import com.kvarovi.app.entity.enums.Priority;
import com.kvarovi.app.entity.enums.ReportStatus;
import com.kvarovi.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FaultReportServiceTest {

    @Mock
    private FaultReportRepository faultReportRepository;
    @Mock
    private StatusHistoryRepository statusHistoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FaultReportService faultReportService;

    private User admin;
    private Building building;
    private Category category;
    private FaultReport report;

    @BeforeEach
    void setUp() {
        building = new Building();
        building.setId(1L);
        building.setName("Zgrada A");
        building.setAddress("Adresa 1");

        category = new Category();
        category.setId(1L);
        category.setName("Vodovod");

        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ROLE_ADMIN");

        admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setFullName("Glavni Administrator");
        admin.setRole(adminRole);
        admin.setBuilding(building);

        report = new FaultReport();
        report.setId(10L);
        report.setDescription("Curi voda u podrumu");
        report.setLocation("Podrum");
        report.setStatus(ReportStatus.PRIJAVLJENO);
        report.setPriority(Priority.SREDNJI);
        report.setCreatedAt(LocalDateTime.now());
        report.setReporter(admin);
        report.setBuilding(building);
        report.setCategory(category);
    }

    @Test
    void updateStatus_uspesnoMenjaStatusIBeleziIstoriju() {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setNewStatus(ReportStatus.U_OBRADI);
        request.setPriority(Priority.VISOK);

        when(faultReportRepository.findById(10L)).thenReturn(Optional.of(report));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(faultReportRepository.save(any(FaultReport.class))).thenReturn(report);

        FaultReportResponse response = faultReportService.updateStatus(10L, "admin", request);

        assertNotNull(response);
        assertEquals(ReportStatus.U_OBRADI, response.getStatus());
        assertEquals(Priority.VISOK, response.getPriority());

        verify(statusHistoryRepository, times(1)).save(any(StatusHistory.class));
        verify(faultReportRepository, times(1)).save(any(FaultReport.class));
    }

    @Test
    void updateStatus_bacaIzuzetakKadaPrijavaNePostoji() {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setNewStatus(ReportStatus.RESENO);

        when(faultReportRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> faultReportService.updateStatus(999L, "admin", request));

        verify(statusHistoryRepository, never()).save(any(StatusHistory.class));
    }
}