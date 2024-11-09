package com.mariaclara.cinevault.repositories;

import com.mariaclara.cinevault.entities.Favorite;
import com.mariaclara.cinevault.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
