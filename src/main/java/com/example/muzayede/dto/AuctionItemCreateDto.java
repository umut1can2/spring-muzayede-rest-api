package com.example.muzayede.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuctionItemCreateDto {
    private String title;
    private String description;
    private BigDecimal startPrice;
    private Integer durationInDays;
    private Long sellerId;
}
