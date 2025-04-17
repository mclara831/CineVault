package com.mariaclara.cinevault.repositories;

import com.mariaclara.cinevault.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(UUID userId);
    List<Review> findByMediaId(Integer mediaId);

    List<Review> findByPublicationDateGreaterThanEqualOrderByRatingDesc(LocalDateTime initialTime);
}
