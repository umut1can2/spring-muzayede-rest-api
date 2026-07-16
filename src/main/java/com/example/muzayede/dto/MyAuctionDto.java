package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyAuctionDto {
    private Long id;
    private String title;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private boolean active;
    private boolean approved;
}
