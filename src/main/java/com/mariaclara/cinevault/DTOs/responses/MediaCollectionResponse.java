package com.mariaclara.cinevault.DTOs.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MediaCollectionResponse {

    private List<MediaResponse> results;

    public MediaCollectionResponse(List<MediaResponse> results) {
        this.results = results;
    }
}
