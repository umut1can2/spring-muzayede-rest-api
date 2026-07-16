package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveBidsDto {
    private Long auctionItemId;
    private String auctionItemTitle;
    private BigDecimal myBid;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
}
