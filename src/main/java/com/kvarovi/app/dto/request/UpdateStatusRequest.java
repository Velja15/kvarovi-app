package com.kvarovi.app.dto.request;

import com.kvarovi.app.entity.enums.Priority;
import com.kvarovi.app.entity.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull(message = "Novi status je obavezan")
    private ReportStatus newStatus;

    private Priority priority;
}