package com.stocksense.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingResponse {

    private Long id;
    private String symbol;
    private String companyName;
    private BigDecimal buyPrice;
    private BigDecimal quantity;
    private BigDecimal totalInvestment;
    private String exchange;
    private LocalDateTime boughtAt;
}