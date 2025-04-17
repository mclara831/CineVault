package com.mariaclara.cinevault.DTOs.responses;

import java.time.LocalDateTime;

public record ReviewResponse(MediaResponse media,
                             Double personalRating,
                             String personalComment,
                             LocalDateTime publicationDate
                             ) {
}
