package com.aura.gallery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "artworks")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double price;

    private String medium;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imagePath;

    @Column(nullable = false)
    private Boolean sold = false;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer stock = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Category {
        Abstract, Landscape, Portrait, Sculpture, Digital
    }

    // Constructors
    public Artwork() {}

    public Artwork(String title, User artist, Category category, Double price, String medium, String description, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.price = price;
        this.medium = medium;
        this.description = description;
        this.imagePath = imagePath;
        this.sold = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getArtist() {
        return artist;
    }

    public Category getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public String getMedium() {
        return medium;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Boolean getSold() {
        return sold;
    }

    public Integer getStock() {
        return stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(User artist) {
        this.artist = artist;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static ArtworkBuilder builder() {
        return new ArtworkBuilder();
    }

    public static class ArtworkBuilder {
        private Long id;
        private String title;
        private User artist;
        private Category category;
        private Double price;
        private String medium;
        private String description;
        private String imagePath;
        private Boolean sold = false;
        private LocalDateTime createdAt;

        public ArtworkBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ArtworkBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArtworkBuilder artist(User artist) {
            this.artist = artist;
            return this;
        }

        public ArtworkBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public ArtworkBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public ArtworkBuilder medium(String medium) {
            this.medium = medium;
            return this;
        }

        public ArtworkBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ArtworkBuilder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public ArtworkBuilder sold(Boolean sold) {
            this.sold = sold;
            return this;
        }

        public ArtworkBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Artwork build() {
            Artwork artwork = new Artwork();
            artwork.id = this.id;
            artwork.title = this.title;
            artwork.artist = this.artist;
            artwork.category = this.category;
            artwork.price = this.price;
            artwork.medium = this.medium;
            artwork.description = this.description;
            artwork.imagePath = this.imagePath;
            artwork.sold = this.sold;
            artwork.createdAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
            return artwork;
        }
    }
}
