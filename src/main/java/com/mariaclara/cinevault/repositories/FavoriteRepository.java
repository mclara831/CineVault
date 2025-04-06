package com.mariaclara.cinevault.repositories;

import com.mariaclara.cinevault.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(UUID userId);
    List<Favorite> findByMediaId(Integer mediaId);
}
