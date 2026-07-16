package com.example.muzayede.repository;

import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
    List<AuctionItem> findByIsActiveTrueAndEndTimeBefore(LocalDateTime dateTime);
    List<AuctionItem> findByIsActiveTrueAndEndTimeAfter(LocalDateTime dateTime);

    @Query("SELECT a FROM AuctionItem a WHERE a.isActive = false " +
            "AND a.approved = true " +
            "AND a.endTime < CURRENT_TIMESTAMP " +
            "AND EXISTS (SELECT b FROM Bid b WHERE b.auctionItem = a " +
            "  AND b.bidder = :user " +
            "  AND b.bidAmount = (SELECT MAX(b2.bidAmount) FROM Bid b2 WHERE b2.auctionItem = a))")
    List<AuctionItem> findWonAuctionsByUser(@Param("user") User user);

    List<AuctionItem> findBySeller(User seller);
}
