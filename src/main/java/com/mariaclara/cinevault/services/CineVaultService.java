package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.clients.CineVaultClient;
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
}
