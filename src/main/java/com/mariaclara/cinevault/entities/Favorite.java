package com.mariaclara.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_favorites")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long media_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Favorite(Long media_id, User user) {
        this.media_id = media_id;
        this.user = user;
    }
}
