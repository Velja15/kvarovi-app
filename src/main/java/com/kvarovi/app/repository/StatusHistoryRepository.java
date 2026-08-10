package com.kvarovi.app.repository;

import com.kvarovi.app.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory> findByReportIdOrderByChangedAtAsc(Long reportId);
}