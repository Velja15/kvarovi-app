package com.kvarovi.app.dto.response;

import com.kvarovi.app.entity.enums.Priority;
import com.kvarovi.app.entity.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FaultReportResponse {

    private Long id;
    private String description;
    private String location;
    private ReportStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private String reporterName;
    private String categoryName;
    private String buildingName;
}