package com.example.muzayede.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal bidAmount;
    private LocalDateTime bidTime;

    @ManyToOne
    private User bidder;

    @ManyToOne
    private AuctionItem auctionItem;

}
