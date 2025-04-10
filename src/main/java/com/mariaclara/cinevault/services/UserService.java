package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.DTOs.requests.LoginRequest;
import com.mariaclara.cinevault.DTOs.requests.RegisterRequest;
import com.mariaclara.cinevault.DTOs.requests.ReviewRequest;
import com.mariaclara.cinevault.DTOs.responses.MediaAddedResponse;
import com.mariaclara.cinevault.DTOs.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.DTOs.responses.MediaResponse;
import com.mariaclara.cinevault.DTOs.responses.ReviewResponse;
import com.mariaclara.cinevault.clients.CineVaultClient;
import com.mariaclara.cinevault.entities.*;
import com.mariaclara.cinevault.repositories.FavoriteRepository;
import com.mariaclara.cinevault.repositories.ReviewRepository;
import com.mariaclara.cinevault.repositories.UserRepository;
import com.mariaclara.cinevault.repositories.WishListRepository;
import com.mariaclara.cinevault.services.exceptions.ResourceAlreadyExistsException;
import com.mariaclara.cinevault.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private CineVaultClient cineVaultClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${api.security.token.expirationTime}")
    private static long secondsToAdd;

    //AUTHENTICATION AND REGISTRATION
    public void register(RegisterRequest data) {
        Optional<User> user = userRepository.findByUsername(data.username());

        if (user.isPresent()) {
            throw new BadCredentialsException("Username already in use!");
        }

        String encodedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.username(), data.email(), encodedPassword, Set.of(data.roles()));
        userRepository.save(newUser);
    }

    public String authenticate(LoginRequest data) {
        var user = userRepository.findByUsername(data.username());

        if (user.isEmpty() || !isLoginCorrect(data, user.get())) {
            throw new BadCredentialsException("Invalid username and/or password!");
        }
        String token = tokenService.generateToken(user.get());
        return token;
    }

    public String dateTimeConveter() {
        Instant expirationTime = Instant.now().plusSeconds(secondsToAdd);

        ZonedDateTime brasiliaTime = expirationTime.atZone(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = brasiliaTime.format(formatter);
        return formattedDate;
    }

    // TOKEN
    public boolean isLoginCorrect(LoginRequest data, User user) {
        return passwordEncoder.matches(data.password(), user.getPassword());
    }

    private User getUserFromToken(JwtAuthenticationToken token) {
        Optional<User> user = userRepository.findById(UUID.fromString(token.getName()));
        if (user.isEmpty())
            throw new UsernameNotFoundException("Invalid token!");

        return user.get();
    }

    //GETTING INFORMATION FROM ID'S
    private <E extends GeneralAttributes> List<MediaResponse> getSeriesFromIds(List<E> desiredMedias) {
        var seriesId = desiredMedias.stream()
                .filter(media -> media.getMediaType().equalsIgnoreCase(MediaType.TV.getMediaType()))
                .map(GeneralAttributes::getMediaId)
                .collect(Collectors.toList());
        var series = seriesId.stream()
                .map(media -> cineVaultClient.getSeriesById(media))
                .peek(media -> media.setMediaType(MediaType.MOVIE.getMediaType()))
                .collect(Collectors.toList());

        return series;
    }

    private <E extends GeneralAttributes> List<MediaResponse> getMoviesFromIds(List<E> desiredMedias) {
        var moviesId = desiredMedias.stream()
                .filter(media -> media.getMediaType().equalsIgnoreCase(MediaType.MOVIE.getMediaType()))
                .map(GeneralAttributes::getMediaId)
                .collect(Collectors.toList());
        var movies = moviesId.stream()
                .map(cineVaultClient::getMovieById)
                .peek(media -> media.setMediaType(MediaType.MOVIE.getMediaType())) // apenas altera, sem quebrar o fluxo
                .collect(Collectors.toList());
        return movies;
    }

    /*****************************************************
     *                 LISTA DE DESEJOS                  *
     *****************************************************/
    public MediaAddedResponse addMediaToWishList(Integer mediaId, JwtAuthenticationToken token, String mediaType) {
        User user = getUserFromToken(token);
        var wishes = wishListRepository.findByUserId(user.getId());

        boolean alreadyExists = wishes.stream()
                .anyMatch(media -> media.getMediaId().equals(mediaId)
                        && media.getMediaType().equalsIgnoreCase(mediaType));

        MediaResponse media;
        if (!alreadyExists) {
            var response = wishListRepository.save(new WishList(mediaId, user, mediaType));
            if (mediaType.equals(MediaType.MOVIE.getMediaType())) {
                media = cineVaultClient.getMovieById(mediaId);
                media.setMediaType(MediaType.MOVIE.getMediaType());
            } else {
                media = cineVaultClient.getSeriesById(mediaId);
                media.setMediaType(MediaType.TV.getMediaType());
            }
            return new MediaAddedResponse(response.getId(), user.getUsername(), media);
        } else {
            throw new ResourceAlreadyExistsException("This object already is in your favorite list!");
        }
    }

    public void removeMediaFromWishList(Integer mediaId, JwtAuthenticationToken token, String mediaType) {
        User user = getUserFromToken(token);
        var wishes = wishListRepository.findByUserId(user.getId());
        boolean found = false;

        for (WishList w : wishes) {
            if (w.getMediaId().equals(mediaId) && w.getMediaType().equalsIgnoreCase(mediaType)) {
                wishListRepository.deleteById(w.getId());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new EntityNotFoundException("Resource not found!");
        }
    }

    public MediaCollectionResponse getAllWishedMedias(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = wishListRepository.findByUserId(user.getId());
        var movies = getMoviesFromIds(mediaIds);
        var series = getSeriesFromIds(mediaIds);
        movies.addAll(series);
        return new MediaCollectionResponse(movies);
    }

    public MediaCollectionResponse getMoviesFromWishList(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = wishListRepository.findByUserId(user.getId());
        var movies = getMoviesFromIds(mediaIds);
        return new MediaCollectionResponse(movies);
    }

    public MediaCollectionResponse getSeriesFromWishList(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = wishListRepository.findByUserId(user.getId());
        var series = getSeriesFromIds(mediaIds);
        return new MediaCollectionResponse(series);
    }

    /*****************************************************
     *                 LISTA DE FAVORITES                  *
     *****************************************************/
    public MediaAddedResponse addMediaToFavoriteList(Integer mediaId, JwtAuthenticationToken token, String mediaType) {
        User user = getUserFromToken(token);
        var favorites = favoriteRepository.findByUserId(user.getId());

        boolean alreadyExists = favorites.stream()
                .anyMatch(media -> media.getMediaId().equals(mediaId)
                        && media.getMediaType().equalsIgnoreCase(mediaType));

        MediaResponse media;
        if (!alreadyExists) {
            var response = favoriteRepository.save(new Favorite(mediaId, user, mediaType));
            if (mediaType.equals(MediaType.MOVIE.getMediaType())) {
                media = cineVaultClient.getMovieById(mediaId);
                media.setMediaType(MediaType.MOVIE.getMediaType());
            } else {
                media = cineVaultClient.getSeriesById(mediaId);
                media.setMediaType(MediaType.TV.getMediaType());
            }
            return new MediaAddedResponse(response.getId(), user.getUsername(), media);
        } else {
            throw new ResourceAlreadyExistsException("This object already is in your favorite list!");
        }
    }

    public void removeMediaFromFavoriteList(Integer mediaId, JwtAuthenticationToken token, String mediaType) {
        User user = getUserFromToken(token);
        var favorites = favoriteRepository.findByUserId(user.getId());
        boolean found = false;
        for (Favorite f : favorites) {
            if (f.getMediaId().equals(mediaId) && f.getMediaType().equalsIgnoreCase(mediaType)) {
                favoriteRepository.deleteById(f.getId());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    public MediaCollectionResponse getAllFavorites(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = favoriteRepository.findByUserId(user.getId());
        var movies = getMoviesFromIds(mediaIds);
        var series = getSeriesFromIds(mediaIds);
        movies.addAll(series);
        return new MediaCollectionResponse(movies);
    }

    public MediaCollectionResponse getFavoritesMovies(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = favoriteRepository.findByUserId(user.getId());
        var movies = getMoviesFromIds(mediaIds);
        return new MediaCollectionResponse(movies);
    }

    public MediaCollectionResponse getFavoritesSeries(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        var mediaIds = favoriteRepository.findByUserId(user.getId());
        var series = getSeriesFromIds(mediaIds);
        return new MediaCollectionResponse(series);
    }

    /*****************************************************
     *                 LISTA DE REVIEWS                  *
     *****************************************************/
    private boolean isValidRating(Double rating) {
        return (rating >= 0.0 && rating <= 10.0);
    }

    public ReviewResponse addReviewToMedia(Integer mediaId, ReviewRequest request, JwtAuthenticationToken token, String mediaType) throws Exception {
        if (!isValidRating(request.rating())) {
            throw new BadRequestException("Rating out of range!");
        }

        User user = getUserFromToken(token);
        var medias = reviewRepository.findByUserId(user.getId());

        boolean alreadyExists = medias.stream().anyMatch(media -> media.getMediaId().equals(mediaId)
                && media.getMediaType().equalsIgnoreCase(mediaType));

        MediaResponse media;
        if (!alreadyExists) {
            var review = new Review(request.rating(), request.comment(), mediaId, mediaType, user);
            reviewRepository.save(review);
            if (mediaType.equals(MediaType.MOVIE.getMediaType())) {
                media = cineVaultClient.getMovieById(mediaId);
                media.setMediaType(MediaType.MOVIE.getMediaType());
            } else {
                media = cineVaultClient.getSeriesById(mediaId);
                media.setMediaType(MediaType.TV.getMediaType());
            }
            return new ReviewResponse(media, review.getRating(), review.getComment());
        } else {
            throw new ResourceAlreadyExistsException("This resource already exists in the repository!");
        }
    }

    public MediaAddedResponse updateReview(Integer mediaId, ReviewRequest request, JwtAuthenticationToken token, String mediaType) {
        var medias = reviewRepository.findByMediaId(mediaId);
        User user = getUserFromToken(token);

        Review newReview = null;
        MediaResponse media;
        boolean found = false;

        for (Review r : medias) {
            if (r.getMediaType().equalsIgnoreCase(mediaType) && r.getUser().equals(user) && r.getMediaId().equals(mediaId)) {
                BeanUtils.copyProperties(request, r);
                newReview = reviewRepository.save(r);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("Resource not found!");
        } else {
            if (mediaType.equals(MediaType.MOVIE.getMediaType())) {
                media = cineVaultClient.getMovieById(mediaId);
                media.setMediaType(MediaType.MOVIE.getMediaType());
            } else {
                media = cineVaultClient.getSeriesById(mediaId);
                media.setMediaType(MediaType.TV.getMediaType());
            }
            return new MediaAddedResponse(newReview.getId(), user.getUsername(), media);
        }
    }

    public void deleteReview(Integer mediaId, JwtAuthenticationToken token, String mediaType) {
        var medias = reviewRepository.findByMediaId(mediaId);
        User user = getUserFromToken(token);
        boolean found = false;
        for (Review r : medias) {
            if (r.getMediaType().equalsIgnoreCase(mediaType) && r.getUser().equals(user) && r.getMediaId().equals(mediaId)) {
                reviewRepository.deleteById(r.getId());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    public List<ReviewResponse> getReviews(JwtAuthenticationToken token) {
        User user = getUserFromToken(token);
        List<Review> userReviews = reviewRepository.findByUserId(user.getId());

        List<MediaResponse> movies = getMoviesFromIds(userReviews);
        List<MediaResponse> series = getSeriesFromIds(userReviews);

        List<ReviewResponse> reviews = new ArrayList<>();

        movies.forEach(mediaResponse -> {
            userReviews.stream()
                    .filter(review -> review.getMediaId().equals(mediaResponse.getId()) &&
                            review.getMediaType().equalsIgnoreCase(MediaType.MOVIE.getMediaType()))
                    .map(review -> {
                        mediaResponse.setMediaType(MediaType.MOVIE.getMediaType());
                        return new ReviewResponse(mediaResponse, review.getRating(), review.getComment());
                    })
                    .forEach(reviews::add);
        });

        series.forEach(mediaResponse -> {
            userReviews.stream()
                    .filter(review -> review.getMediaId().equals(mediaResponse.getId()) &&
                            review.getMediaType().equalsIgnoreCase(MediaType.TV.getMediaType()))
                    .map(review -> {
                        mediaResponse.setMediaType(MediaType.TV.getMediaType());
                        return new ReviewResponse(mediaResponse, review.getRating(), review.getComment());
                    })
                    .forEach(reviews::add);
        });
        return reviews;
    }
}
