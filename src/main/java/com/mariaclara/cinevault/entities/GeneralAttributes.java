package com.mariaclara.cinevault.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class GeneralAttributes {

    private Integer mediaId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String mediaType;
}
