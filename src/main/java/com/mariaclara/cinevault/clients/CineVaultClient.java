package com.mariaclara.cinevault.clients;

import com.mariaclara.cinevault.clients.config.FeingConfig;
import com.mariaclara.cinevault.clients.responses.MediaCollectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "CineVaultClient",
        url = "https://api.themoviedb.org/3",
        configuration = FeingConfig.class
)
public interface CineVaultClient {

    @GetMapping("/trending/movie/week")
    MediaCollectionResponse trendingAll();
}
