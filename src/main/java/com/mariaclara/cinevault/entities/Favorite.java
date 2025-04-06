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
public class Favorite extends GeneralAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Favorite(Integer media_id, User user, String mediaType) {
        super(media_id, user, mediaType);
    }
}
