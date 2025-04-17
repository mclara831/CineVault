package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.DTOs.requests.SearchMediaRequest;
import com.mariaclara.cinevault.DTOs.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.DTOs.responses.RankingResponse;
import com.mariaclara.cinevault.clients.CineVaultClient;
import com.mariaclara.cinevault.entities.MediaType;
import com.mariaclara.cinevault.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CineVaultService {

    @Autowired
    private CineVaultClient cineVaultClient;

    @Autowired
    private ReviewRepository reviewRepository;

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

    public List<RankingResponse> getWeeklyRanking() {
        var initialDate = LocalDateTime.now().minusWeeks(1);
        var result = reviewRepository.findByPublicationDateGreaterThanEqualOrderByRatingDesc(initialDate);

        List<RankingResponse> ranking = result.stream().map(review -> {
            if (review.getMediaType().equalsIgnoreCase(MediaType.MOVIE.getMediaType())) {
                var media = cineVaultClient.getMovieById(review.getMediaId());
                return new RankingResponse(review.getUser().getUsername(),
                                            media.getDisplayTitle(),
                                            review.getRating(),
                                            review.getComment(),
                                            review.getPublicationDate());
            } else {
                var media = cineVaultClient.getSeriesById(review.getMediaId());
                return new RankingResponse(review.getUser().getUsername(),
                        media.getDisplayTitle(),
                        review.getRating(),
                        review.getComment(),
                        review.getPublicationDate());
            }
        }).collect(Collectors.toList());
        return ranking;
    }
}
