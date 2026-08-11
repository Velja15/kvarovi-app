package com.kvarovi.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFaultReportRequest {

    @NotBlank(message = "Opis je obavezan")
    private String description;

    @NotBlank(message = "Lokacija je obavezna")
    private String location;

    @NotNull(message = "Kategorija je obavezna")
    private Long categoryId;
}