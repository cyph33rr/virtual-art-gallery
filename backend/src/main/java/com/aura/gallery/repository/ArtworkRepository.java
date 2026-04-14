package com.aura.gallery.repository;

import com.aura.gallery.model.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findAllByOrderByCreatedAtDesc();
    List<Artwork> findByCategoryOrderByCreatedAtDesc(Artwork.Category category);
    List<Artwork> findByArtistIdOrderByCreatedAtDesc(Long artistId);
    List<Artwork> findBySoldFalseOrderByCreatedAtDesc();
}
