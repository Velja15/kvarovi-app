package com.kvarovi.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Korisnicko ime je obavezno")
    private String username;

    @NotBlank(message = "Lozinka je obavezna")
    private String password;

    @NotBlank(message = "Ime i prezime je obavezno")
    private String fullName;

    private Long buildingId;
}