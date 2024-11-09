package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.clients.CineVaultClient;
import com.mariaclara.cinevault.clients.requests.SearchMediaRequest;
import com.mariaclara.cinevault.clients.responses.MediaCollectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CineVaultService {

    @Autowired
    private CineVaultClient cineVaultClient;

    public MediaCollectionResponse trendingAll() {
        return cineVaultClient.trendingAll();
    }

    public MediaCollectionResponse popularMedia(String media) {
        return cineVaultClient.popularMedia(media);
    }
    public MediaCollectionResponse searchMedia(SearchMediaRequest request) {
        return cineVaultClient.searchMedia(request.media(), request.mediaName(), request.year());
    }
}
