package com.mariaclara.cinevault.repositories;

import com.mariaclara.cinevault.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
}
