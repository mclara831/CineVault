package com.mariaclara.cinevault.controllers;

import com.mariaclara.cinevault.clients.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.services.CineVaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cine")
public class CineVaultController {

    @Autowired
    private CineVaultService cineVaultService;

    @GetMapping
    ResponseEntity<MediaCollectionResponse> trendingAll() {
        return ResponseEntity.ok(cineVaultService.trendingAll());
    }
}
