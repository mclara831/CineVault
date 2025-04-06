package com.mariaclara.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_wishes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class WishList extends GeneralAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public WishList(Integer media_id, User user, String mediaType) {
        super(media_id, user, mediaType);
    }
}
