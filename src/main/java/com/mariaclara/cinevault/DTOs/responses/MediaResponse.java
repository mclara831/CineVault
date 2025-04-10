package com.mariaclara.cinevault.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {

    private Integer id;
    private String title;
    private String name;
    private String overview;
    private String mediaType;
    private String releaseDate;
    private String firstAirDate;
    private Double voteAverage;

    @JsonGetter("title")
    public String getDisplayTitle() {
        return title != null ? title : name;
    }

    @JsonGetter("release_Date")
    public String getDisplayDate() {
        return releaseDate != null ? releaseDate : firstAirDate;
    }

    public Integer getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
