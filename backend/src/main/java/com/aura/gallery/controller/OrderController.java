package com.aura.gallery.controller;

import com.aura.gallery.model.*;
import com.aura.gallery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderRepository orderRepo;
    @Autowired private ArtworkRepository artworkRepo;
    @Autowired private UserRepository userRepo;

    // ── CHECKOUT: create an order from a list of artwork IDs ──
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req, Authentication auth) {
        User buyer = userRepo.findByEmail(auth.getName()).orElseThrow();

        List<Artwork> artworks = artworkRepo.findAllById(req.getArtworkIds());
        List<Artwork> unavailable = artworks.stream().filter(Artwork::getSold).collect(Collectors.toList());
        if (!unavailable.isEmpty()) {
            return ResponseEntity.badRequest().body("Some artworks are already sold: " +
                unavailable.stream().map(Artwork::getTitle).collect(Collectors.joining(", ")));
        }

        // Mark all as sold
        artworks.forEach(a -> a.setSold(true));
        artworkRepo.saveAll(artworks);

        double total = artworks.stream().mapToDouble(Artwork::getPrice).sum();

        Order order = Order.builder()
                .buyer(buyer)
                .artworks(artworks)
                .totalAmount(total)
                .build();
        orderRepo.save(order);

        return ResponseEntity.ok(new OrderSummary(order.getId(), total,
            artworks.stream().map(Artwork::getTitle).collect(Collectors.toList()),
            order.getOrderedAt().toString()));
    }

    // ── GET MY ORDERS ─────────────────────────────
    @GetMapping("/my")
    public List<OrderSummary> getMyOrders(Authentication auth) {
        User buyer = userRepo.findByEmail(auth.getName()).orElseThrow();
        return orderRepo.findByBuyerIdOrderByOrderedAtDesc(buyer.getId())
            .stream()
            .map(o -> new OrderSummary(o.getId(), o.getTotalAmount(),
                o.getArtworks().stream().map(Artwork::getTitle).collect(Collectors.toList()),
                o.getOrderedAt().toString()))
            .collect(Collectors.toList());
    }

    static class CheckoutRequest {
        List<Long> artworkIds;
        
        public List<Long> getArtworkIds() { return artworkIds; }
        public void setArtworkIds(List<Long> artworkIds) { this.artworkIds = artworkIds; }
    }

    static class OrderSummary {
        Long orderId; Double totalAmount; List<String> artworkTitles; String orderedAt;
        
        public OrderSummary() {}
        public OrderSummary(Long orderId, Double totalAmount, List<String> artworkTitles, String orderedAt) {
            this.orderId = orderId;
            this.totalAmount = totalAmount;
            this.artworkTitles = artworkTitles;
            this.orderedAt = orderedAt;
        }
        
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
        public List<String> getArtworkTitles() { return artworkTitles; }
        public void setArtworkTitles(List<String> artworkTitles) { this.artworkTitles = artworkTitles; }
        public String getOrderedAt() { return orderedAt; }
        public void setOrderedAt(String orderedAt) { this.orderedAt = orderedAt; }
    }
}
