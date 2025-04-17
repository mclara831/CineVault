package com.mariaclara.cinevault.DTOs.responses;

import java.time.LocalDateTime;

public record RankingResponse(String username,
                              String mediaName,
                              Double personalRating,
                              String personalComment,
                              LocalDateTime publicationDate) {
}
