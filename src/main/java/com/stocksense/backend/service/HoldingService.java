package com.stocksense.backend.service;

import com.stocksense.backend.dto.HoldingRequest;
import com.stocksense.backend.dto.HoldingResponse;
import com.stocksense.backend.model.Holding;
import com.stocksense.backend.model.User;
import com.stocksense.backend.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoldingService {

    private final HoldingRepository holdingRepository;

    // Add a new stock to portfolio
    public HoldingResponse addHolding(HoldingRequest request, User user) {

        // Check if user already has this stock
        if (holdingRepository.existsByUserIdAndSymbol(
                user.getId(), request.getSymbol().toUpperCase())) {
            throw new RuntimeException(
                    "You already have " + request.getSymbol() +
                            " in your portfolio. Use update instead."
            );
        }

        Holding holding = Holding.builder()
                .user(user)
                .symbol(request.getSymbol().toUpperCase())
                .companyName(request.getCompanyName())
                .buyPrice(request.getBuyPrice())
                .quantity(request.getQuantity())
                .exchange(request.getExchange().toUpperCase())
                .build();

        Holding saved = holdingRepository.save(holding);
        return mapToResponse(saved);
    }

    // Get all holdings for the logged-in user
    public List<HoldingResponse> getAllHoldings(User user) {
        return holdingRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get a single holding by id
    public HoldingResponse getHolding(Long id, User user) {
        Holding holding = holdingRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Holding not found")
                );
        return mapToResponse(holding);
    }

    // Update an existing holding
    public HoldingResponse updateHolding(Long id,
                                         HoldingRequest request,
                                         User user) {
        Holding holding = holdingRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Holding not found")
                );

        holding.setBuyPrice(request.getBuyPrice());
        holding.setQuantity(request.getQuantity());
        holding.setCompanyName(request.getCompanyName());
        holding.setExchange(request.getExchange().toUpperCase());

        Holding updated = holdingRepository.save(holding);
        return mapToResponse(updated);
    }

    // Delete a holding
    public void deleteHolding(Long id, User user) {
        Holding holding = holdingRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Holding not found")
                );
        holdingRepository.delete(holding);
    }

    // Convert Holding model to HoldingResponse DTO
    private HoldingResponse mapToResponse(Holding holding) {
        return HoldingResponse.builder()
                .id(holding.getId())
                .symbol(holding.getSymbol())
                .companyName(holding.getCompanyName())
                .buyPrice(holding.getBuyPrice())
                .quantity(holding.getQuantity())
                .totalInvestment(holding.getTotalInvestment())
                .exchange(holding.getExchange())
                .boughtAt(holding.getBoughtAt())
                .build();
    }
}