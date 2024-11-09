package com.mariaclara.cinevault.clients;

import com.mariaclara.cinevault.clients.config.FeingConfig;
import com.mariaclara.cinevault.clients.requests.SearchMediaRequest;
import com.mariaclara.cinevault.clients.responses.MediaCollectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "CineVaultClient",
        url = "https://api.themoviedb.org/3",
        configuration = FeingConfig.class
)
public interface CineVaultClient {

    @GetMapping("/trending/movie/week")
    MediaCollectionResponse trendingAll();

    @GetMapping("/search/{media}")
    MediaCollectionResponse searchMedia(
            @RequestParam(value = "media") String media,
            @RequestParam(value = "query") String movieName,
            @RequestParam(value = "year", required = false) String year);
}
