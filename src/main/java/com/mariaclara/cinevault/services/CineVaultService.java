package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.DTOs.responses.MediaResponse;
import com.mariaclara.cinevault.clients.CineVaultClient;
import com.mariaclara.cinevault.DTOs.requests.SearchMediaRequest;
import com.mariaclara.cinevault.DTOs.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.entities.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CineVaultService {

    @Autowired
    private CineVaultClient cineVaultClient;

    public MediaCollectionResponse trendingAll() {
        return cineVaultClient.trendingAll();
    }

    public MediaCollectionResponse popularMedia(String mediaType) {
        var results = cineVaultClient.popularMedia(mediaType);
        results.getResults().stream().peek(mediaResponse -> mediaResponse.setMediaType(mediaType))
                .collect(Collectors.toList());
        return results;
    }

    public MediaCollectionResponse searchMedia(SearchMediaRequest request) {
        var results =  cineVaultClient.searchMedia(request.media(), request.mediaName(), request.year());
        String mediaType = request.media().equals(MediaType.MOVIE.toString()) ? MediaType.MOVIE.toString(): MediaType.TV.toString();
        results.getResults().stream().peek(mediaResponse -> mediaResponse.setMediaType(mediaType))
                .collect(Collectors.toList());
        return results;
    }
}
