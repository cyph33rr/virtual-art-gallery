package com.aura.gallery.repository;

import com.aura.gallery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerIdOrderByOrderedAtDesc(Long buyerId);
}
