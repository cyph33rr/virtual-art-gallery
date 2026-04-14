package com.aura.gallery.controller;

import com.aura.gallery.model.Artwork;
import com.aura.gallery.model.User;
import com.aura.gallery.repository.ArtworkRepository;
import com.aura.gallery.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    @Autowired private ArtworkRepository artworkRepo;
    @Autowired private UserRepository userRepo;

    @Value("${aura.upload.dir}")
    private String uploadDir;

    // ── GET ALL ──────────────────────────────────
    @GetMapping
    public List<ArtworkDTO> getAllArtworks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean availableOnly) {

        List<Artwork> artworks;
        if (category != null) {
            Artwork.Category cat = Artwork.Category.valueOf(category);
            artworks = artworkRepo.findByCategoryOrderByCreatedAtDesc(cat);
        } else if (Boolean.TRUE.equals(availableOnly)) {
            artworks = artworkRepo.findBySoldFalseOrderByCreatedAtDesc();
        } else {
            artworks = artworkRepo.findAllByOrderByCreatedAtDesc();
        }
        return artworks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── GET ONE ──────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ArtworkDTO> getArtwork(@PathVariable Long id) {
        return artworkRepo.findById(id)
                .map(a -> ResponseEntity.ok(toDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── UPLOAD (Artist only) ──────────────────────
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadArtwork(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam Double price,
            @RequestParam(required = false) String medium,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image,
            Authentication auth) {

        User artist = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (artist.getRole() != User.Role.ARTIST) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only artists can upload artworks");
        }

        // Check for duplicate title (case-insensitive)
        boolean titleExists = artworkRepo.findAll().stream()
                .anyMatch(a -> a.getTitle().equalsIgnoreCase(title));
        if (titleExists) {
            return ResponseEntity.badRequest().body("An artwork with this title already exists. Please use a unique title.");
        }

        // Save image file if provided
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename()
                        .replaceAll("[^a-zA-Z0-9._-]", "_");
                Path filePath = uploadPath.resolve(filename);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = filename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to save image: " + e.getMessage());
            }
        }

        Artwork artwork = Artwork.builder()
                .title(title)
                .artist(artist)
                .category(Artwork.Category.valueOf(category))
                .price(price)
                .medium(medium)
                .description(description)
                .imagePath(imagePath)
                .sold(false)
                .build();

        artworkRepo.save(artwork);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(artwork));
    }

    // ── MARK SOLD ────────────────────────────────
    @PutMapping("/{id}/purchase")
    public ResponseEntity<?> purchaseArtwork(@PathVariable Long id, Authentication auth) {
        return artworkRepo.findById(id).map(artwork -> {
            if (artwork.getSold()) {
                return ResponseEntity.badRequest().body("Artwork already sold");
            }
            artwork.setSold(true);
            artworkRepo.save(artwork);
            return ResponseEntity.ok(toDTO(artwork));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE (artist can delete own) ───────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id, Authentication auth) {
        return artworkRepo.findById(id).map(artwork -> {
            if (!artwork.getArtist().getEmail().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your artwork");
            }
            artworkRepo.delete(artwork);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── GET BY ARTIST ─────────────────────────────
    @GetMapping("/my")
    public List<ArtworkDTO> getMyArtworks(Authentication auth) {
        User artist = userRepo.findByEmail(auth.getName()).orElseThrow();
        return artworkRepo.findByArtistIdOrderByCreatedAtDesc(artist.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ── DTO MAPPING ───────────────────────────────
    private ArtworkDTO toDTO(Artwork a) {
        return new ArtworkDTO(
            a.getId(), a.getTitle(),
            a.getArtist().getName(), a.getArtist().getId(),
            a.getCategory().name(), a.getPrice(),
            a.getMedium(), a.getDescription(),
            a.getImagePath() != null ? "/uploads/" + a.getImagePath() : null,
            a.getSold(), a.getStock(), a.getCreatedAt() != null ? a.getCreatedAt().toString() : null
        );
    }

    public static class ArtworkDTO {
        Long id; String title; String artistName; Long artistId;
        String category; Double price; String medium; String description;
        String imageUrl; Boolean sold; Integer stock; String createdAt;
        
        public ArtworkDTO() {}
        public ArtworkDTO(Long id, String title, String artistName, Long artistId,
                String category, Double price, String medium, String description,
                String imageUrl, Boolean sold, Integer stock, String createdAt) {
            this.id = id;
            this.title = title;
            this.artistName = artistName;
            this.artistId = artistId;
            this.category = category;
            this.price = price;
            this.medium = medium;
            this.description = description;
            this.imageUrl = imageUrl;
            this.sold = sold;
            this.stock = stock;
            this.createdAt = createdAt;
        }
        
        public Long getId() { return id; }
        public Integer getStock() { return stock; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getArtistName() { return artistName; }
        public void setArtistName(String artistName) { this.artistName = artistName; }
        public Long getArtistId() { return artistId; }
        public void setArtistId(Long artistId) { this.artistId = artistId; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getMedium() { return medium; }
        public void setMedium(String medium) { this.medium = medium; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Boolean getSold() { return sold; }
        public void setSold(Boolean sold) { this.sold = sold; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}
