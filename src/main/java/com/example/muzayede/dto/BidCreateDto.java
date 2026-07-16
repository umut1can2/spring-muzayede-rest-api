package com.example.muzayede.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidCreateDto {
    private Long auctionItemId;
    private BigDecimal bidAmount;
}
