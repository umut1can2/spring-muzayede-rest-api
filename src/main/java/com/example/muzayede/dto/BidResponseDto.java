package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidResponseDto {
    private Long auctionItemId;
    private BigDecimal currentPrice;
    private String bidderUsername;
    private LocalDateTime bidTime;
}
