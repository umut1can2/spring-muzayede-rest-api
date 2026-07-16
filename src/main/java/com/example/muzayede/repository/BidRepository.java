package com.example.muzayede.repository;

import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b from Bid b where b.auctionItem= :item order by b.bidAmount desc limit 1")
    Optional<Bid> findHighestBidForItem(@Param("item") AuctionItem item);
    List<Bid> findByAuctionItemOrderByBidAmountDesc(AuctionItem auctionItem);

    @Query("SELECT b FROM Bid b WHERE b.bidder = :user " +
            "AND b.auctionItem.isActive = true " +
            "AND b.auctionItem.endTime > CURRENT_TIMESTAMP " +
            "AND b.bidAmount = (SELECT MAX(b2.bidAmount) FROM Bid b2 WHERE b2.bidder = :user AND b2.auctionItem = b.auctionItem)")
    List<Bid> findActiveBidsByUser(@Param("user") User user);
}
