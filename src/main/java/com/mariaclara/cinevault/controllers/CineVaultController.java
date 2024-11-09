package com.mariaclara.cinevault.controllers;

import com.mariaclara.cinevault.clients.requests.SearchMediaRequest;
import com.mariaclara.cinevault.clients.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.services.CineVaultService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/popular")
    ResponseEntity<MediaCollectionResponse> popular(@RequestParam @NotNull @NotBlank String media) {
        return ResponseEntity.ok(cineVaultService.popularMedia(media));
    }

    @GetMapping("/search")
    ResponseEntity<MediaCollectionResponse> searchMedia(
            @RequestParam String media,
            @RequestParam String mediaName,
            @RequestParam(required = false) String year
    ) {
        var request = new SearchMediaRequest(media, mediaName, year);
        return  ResponseEntity.ok(cineVaultService.searchMedia(request));
    }
}
