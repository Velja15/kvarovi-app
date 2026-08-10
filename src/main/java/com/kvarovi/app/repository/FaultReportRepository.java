package com.kvarovi.app.repository;

import com.kvarovi.app.entity.FaultReport;
import com.kvarovi.app.entity.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaultReportRepository extends JpaRepository<FaultReport, Long> {

    List<FaultReport> findByReporterId(Long reporterId);

    List<FaultReport> findByBuildingId(Long buildingId);

    List<FaultReport> findByBuildingIdAndStatus(Long buildingId, ReportStatus status);

    List<FaultReport> findByBuildingIdAndCategoryId(Long buildingId, Long categoryId);
}