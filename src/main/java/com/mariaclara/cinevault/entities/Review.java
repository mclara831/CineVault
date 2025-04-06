package com.mariaclara.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Review extends GeneralAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double rating;
    private String comment;

    public Review(Double rating, String comment, Integer mediaId, String mediaType, User user) {
        super(mediaId, user, mediaType);
        this.rating = rating;
        this.comment = comment;
    }
}
