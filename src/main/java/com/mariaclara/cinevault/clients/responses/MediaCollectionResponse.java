package com.mariaclara.cinevault.clients.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MediaCollectionResponse {

    private List<MediaResponse> results;
}
