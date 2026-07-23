package com.stocksense.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user owns this holding
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String symbol; // e.g. RELIANCE, TCS, INFY

    @Column(nullable = false)
    private String companyName; // e.g. Reliance Industries Ltd

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal buyPrice; // price per share when bought

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // number of shares

    @Column(nullable = false)
    private String exchange; // NSE or BSE

    @Column(nullable = false, updatable = false)
    private LocalDateTime boughtAt;

    @PrePersist
    protected void onCreate() {
        boughtAt = LocalDateTime.now();
    }

    // Calculated field - not stored in DB
    @Transient
    public BigDecimal getTotalInvestment() {
        return buyPrice.multiply(quantity);
    }
}