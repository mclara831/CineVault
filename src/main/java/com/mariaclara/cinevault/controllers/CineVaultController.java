package com.mariaclara.cinevault.controllers;

import com.mariaclara.cinevault.DTOs.requests.ReviewRequest;
import com.mariaclara.cinevault.DTOs.requests.SearchMediaRequest;
import com.mariaclara.cinevault.DTOs.responses.MediaAddedResponse;
import com.mariaclara.cinevault.DTOs.responses.MediaCollectionResponse;
import com.mariaclara.cinevault.DTOs.responses.ReviewResponse;
import com.mariaclara.cinevault.services.CineVaultService;
import com.mariaclara.cinevault.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cine", produces = {"application/json"})
@Tag(name = "cine")
public class CineVaultController {

    @Autowired
    private CineVaultService cineVaultService;

    @Autowired
    private UserService userService;

    //GETTING MOVIES FROM THE EXTERNAL API
    @Operation(summary = "Busca as filmes/séries que estão em alta", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping
    ResponseEntity<MediaCollectionResponse> trendingAll() {
        return ResponseEntity.ok(cineVaultService.trendingAll());
    }

    @Operation(summary = "Busca as filmes/séries são populares", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/popular")
    ResponseEntity<MediaCollectionResponse> popular(@RequestParam @NotNull @NotBlank String media) {
        return ResponseEntity.ok(cineVaultService.popularMedia(media));
    }

    @Operation(summary = "Busca as filmes/séries por nome", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/search")
    ResponseEntity<MediaCollectionResponse> searchMedia(
            @RequestParam String media,
            @RequestParam String mediaName,
            @RequestParam(required = false) String year
    ) {
        var request = new SearchMediaRequest(media, mediaName, year);
        return ResponseEntity.ok(cineVaultService.searchMedia(request));
    }

    /*****************************************************
     *                 LISTA DE DESEJOS                  *
     *****************************************************/

    @Operation(summary = "Adiciona filmes/séries à lista de desejos do usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @PostMapping("/{mediaId}/wishlist")
    ResponseEntity<MediaAddedResponse> addMediaToWishList(@PathVariable Integer mediaId,
                                                          JwtAuthenticationToken token,
                                                          @RequestParam String mediaType) {
        var result = userService.addMediaToWishList(mediaId, token, mediaType);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Remove filmes/séries da lista de desejos do usuário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @DeleteMapping("/{mediaId}/wishlist")
    ResponseEntity<MediaAddedResponse> removeMediaFromWishList(@PathVariable Integer mediaId,
                                                JwtAuthenticationToken token,
                                                @RequestParam String mediaType) {
        userService.removeMediaFromWishList(mediaId, token, mediaType);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Busca todos filmes/séries da lista de desejos do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/wishes")
    ResponseEntity<MediaCollectionResponse> getAllMediasFromWishLIst(JwtAuthenticationToken token) {
        var favorites = userService.getAllWishedMedias(token);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Busca todos os filmes da lista de desejos do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/wishes/movies")
    ResponseEntity<MediaCollectionResponse> getMoviesFromWishList(JwtAuthenticationToken token) {
        var favorites = userService.getMoviesFromWishList(token);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Busca todos as séries da lista de desejos do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/wishes/series")
    ResponseEntity<MediaCollectionResponse> getSeriesFromWishList(JwtAuthenticationToken token) {
        var favorites = userService.getSeriesFromWishList(token);
        return ResponseEntity.ok(favorites);
    }

    /*****************************************************
     *                 LISTA DE FAVORITES                  *
     *****************************************************/

    @Operation(summary = "Adiciona filmes/séries da lista de favoritos do usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @PostMapping("/{mediaId}/favorite")
    ResponseEntity<MediaAddedResponse> addMediaToFavoriteList(@PathVariable Integer mediaId,
                                             JwtAuthenticationToken token,
                                             @RequestParam String mediaType){
        var response = userService.addMediaToFavoriteList(mediaId, token, mediaType);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Deleta filmes/séries da lista de favoritos do usuário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @DeleteMapping("/{mediaId}/favorite")
    ResponseEntity<Void> removeMediaFromFavoriteList(@PathVariable Integer mediaId,
                                            JwtAuthenticationToken token,
                                            @RequestParam String mediaType) {
        userService.removeMediaFromFavoriteList(mediaId, token, mediaType);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Busca todos os filmes/séries da lista de favoritos do usuários", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/favorites")
    ResponseEntity<MediaCollectionResponse> getAllFavorites(JwtAuthenticationToken token) {
        var favorites = userService.getAllFavorites(token);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Busca todos os filmes da lista de favoritos do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/favorites/movies")
    ResponseEntity<MediaCollectionResponse> getFavoritesMovies(JwtAuthenticationToken token) {
        var favorites = userService.getFavoritesMovies(token);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Busca todos as séries da lista de favoritos do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/favorites/series")
    ResponseEntity<MediaCollectionResponse> getFavoritesSeries(JwtAuthenticationToken token) {
        var favorites = userService.getFavoritesSeries(token);
        return ResponseEntity.ok(favorites);
    }

    /*****************************************************
     *                 LISTA DE REVIEWS                  *
     *****************************************************/

    @Operation(summary = "Busca todos filmes/séries da lista de revisões do usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @GetMapping("/user/reviews")
    ResponseEntity<List<ReviewResponse>> getAllReviewedMedias(JwtAuthenticationToken token) {
        var revieweds = userService.getReviews(token);
        return ResponseEntity.ok(revieweds);
    }

    @Operation(summary = "Adiciona avaliação de filmes/séries da lista de revisados do usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @PostMapping("/review/{mediaId}")
    ResponseEntity<ReviewResponse> addReviewToMovie(@PathVariable Integer mediaId,
                                          @RequestBody ReviewRequest request,
                                          JwtAuthenticationToken token,
                                          @RequestParam String mediaType) throws Exception {
        var response = userService.addReviewToMedia(mediaId, request, token, mediaType);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Atualiza uma avaliação de filmes/séries da lista de revisados do usuário", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @PutMapping("/user/reviews/{mediaId}")
    ResponseEntity<MediaAddedResponse> updateReviewByMediaId(JwtAuthenticationToken token,
                                               @RequestBody ReviewRequest request,
                                               @PathVariable Integer mediaId,
                                               @RequestParam String mediaType) {
        var response = userService.updateReview(mediaId,request, token, mediaType);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Remove avaliações de filmes/séries da lista de revisados do usuário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploads de mídias realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido!"),
            @ApiResponse(responseCode = "405", description = "Recurso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o upload de mídias ")
    })
    @DeleteMapping("/user/reviews/{mediaId}")
    ResponseEntity<Void> deleteReviewByMediaId(JwtAuthenticationToken token,
                                               @PathVariable Integer mediaId,
                                               @RequestParam String mediaType) {
        userService.deleteReview(mediaId, token, mediaType);
        return  ResponseEntity.ok().build();
    }
}

