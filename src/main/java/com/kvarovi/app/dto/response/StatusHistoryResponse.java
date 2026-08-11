package com.kvarovi.app.dto.response;

import com.kvarovi.app.entity.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatusHistoryResponse {

    private ReportStatus oldStatus;
    private ReportStatus newStatus;
    private String changedByName;
    private LocalDateTime changedAt;
}