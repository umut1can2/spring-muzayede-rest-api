package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidHistoryDto {
    private String bidderUserName;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
}
