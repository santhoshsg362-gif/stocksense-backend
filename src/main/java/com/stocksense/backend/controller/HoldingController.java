package com.stocksense.backend.controller;

import com.stocksense.backend.dto.HoldingRequest;
import com.stocksense.backend.dto.HoldingResponse;
import com.stocksense.backend.model.User;
import com.stocksense.backend.service.HoldingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HoldingController {

    private final HoldingService holdingService;

    // POST /api/portfolio/holdings - Add a stock
    @PostMapping("/holdings")
    public ResponseEntity<HoldingResponse> addHolding(
            @Valid @RequestBody HoldingRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(holdingService.addHolding(request, user));
    }

    // GET /api/portfolio/holdings - Get all my stocks
    @GetMapping("/holdings")
    public ResponseEntity<List<HoldingResponse>> getAllHoldings(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(holdingService.getAllHoldings(user));
    }

    // GET /api/portfolio/holdings/{id} - Get one stock
    @GetMapping("/holdings/{id}")
    public ResponseEntity<HoldingResponse> getHolding(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(holdingService.getHolding(id, user));
    }

    // PUT /api/portfolio/holdings/{id} - Update a stock
    @PutMapping("/holdings/{id}")
    public ResponseEntity<HoldingResponse> updateHolding(
            @PathVariable Long id,
            @Valid @RequestBody HoldingRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                holdingService.updateHolding(id, request, user)
        );
    }

    // DELETE /api/portfolio/holdings/{id} - Remove a stock
    @DeleteMapping("/holdings/{id}")
    public ResponseEntity<Void> deleteHolding(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        holdingService.deleteHolding(id, user);
        return ResponseEntity.noContent().build();
    }
}