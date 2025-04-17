package com.mariaclara.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Review extends GeneralAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double rating;
    private String comment;

    @CreatedDate
    private LocalDateTime publicationDate;

    public Review(Double rating, String comment, Integer mediaId, String mediaType, User user) {
        super(mediaId, user, mediaType);
        this.rating = rating;
        this.comment = comment;
    }
}
