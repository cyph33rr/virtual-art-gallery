package com.aura.gallery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToMany
    @JoinTable(
        name = "order_artworks",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    private List<Artwork> artworks;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    @PrePersist
    protected void onCreate() {
        if (orderedAt == null) {
            orderedAt = LocalDateTime.now();
        }
    }

    // Constructors
    public Order() {}

    public Order(User buyer, List<Artwork> artworks, Double totalAmount) {
        this.buyer = buyer;
        this.artworks = artworks;
        this.totalAmount = totalAmount;
        this.orderedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    // Builder
    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Long id;
        private User buyer;
        private List<Artwork> artworks;
        private Double totalAmount;
        private LocalDateTime orderedAt;

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public OrderBuilder artworks(List<Artwork> artworks) {
            this.artworks = artworks;
            return this;
        }

        public OrderBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderBuilder orderedAt(LocalDateTime orderedAt) {
            this.orderedAt = orderedAt;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.id = this.id;
            order.buyer = this.buyer;
            order.artworks = this.artworks;
            order.totalAmount = this.totalAmount;
            order.orderedAt = this.orderedAt != null ? this.orderedAt : LocalDateTime.now();
            return order;
        }
    }
}
