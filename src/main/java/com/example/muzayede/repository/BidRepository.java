package com.example.muzayede.repository;

import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b from Bid b where b.auctionItem= :item order by b.bidAmount desc limit 1")
    Optional<Bid> findHighestBidForItem(@Param("item") AuctionItem item);
}
