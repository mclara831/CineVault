package com.mariaclara.cinevault.DTOs.responses;

import java.time.LocalDateTime;

public record MediaAddedResponse(Long id, String username, MediaResponse mediaResponse) {

}
