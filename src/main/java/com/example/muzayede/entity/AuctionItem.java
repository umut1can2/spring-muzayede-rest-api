package com.example.muzayede.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class AuctionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private boolean isActive;
    private boolean approved;
    @ManyToOne
    private User seller;

    @ManyToOne
    private User approvedBy;

}
