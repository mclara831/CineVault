package com.mariaclara.cinevault.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SearchMediaRequest(@NotNull @NotBlank String media, @NotNull @NotBlank String mediaName, String year) {
}
