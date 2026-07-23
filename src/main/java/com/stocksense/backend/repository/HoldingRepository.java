package com.stocksense.backend.repository;

import com.stocksense.backend.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // Get all holdings for a specific user
    List<Holding> findByUserId(Long userId);

    // Get a specific holding by id and user
    // (prevents users from seeing each other's holdings)
    Optional<Holding> findByIdAndUserId(Long id, Long userId);

    // Check if user already has this stock
    boolean existsByUserIdAndSymbol(Long userId, String symbol);

    // Get holding by symbol for a user
    Optional<Holding> findByUserIdAndSymbol(Long userId, String symbol);
}