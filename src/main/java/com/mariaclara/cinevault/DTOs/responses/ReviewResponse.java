package com.mariaclara.cinevault.DTOs.responses;

public record ReviewResponse(MediaResponse media,
                             Double personalRating,
                             String personalComment
                             ) {
}
