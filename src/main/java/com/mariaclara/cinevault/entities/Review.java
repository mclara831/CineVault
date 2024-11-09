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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
