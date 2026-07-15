package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AuctionListItemDto {
    private String title;
    private String description;
    private BigDecimal currentPrice;
    private LocalDateTime auctionEndTime;
    private String sellerName;
}
