package com.mariaclara.cinevault.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MediaType {
    TV("tv"),
    MOVIE("movie");

    String mediaType;
}
