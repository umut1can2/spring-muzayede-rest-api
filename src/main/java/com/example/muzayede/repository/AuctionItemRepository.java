package com.example.muzayede.repository;

import com.example.muzayede.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
    List<AuctionItem> findByIsActiveTrueAndEndTimeBefore(LocalDateTime dateTime);
    List<AuctionItem> findByIsActiveTrueAndEndTimeAfter(LocalDateTime dateTime);
}
