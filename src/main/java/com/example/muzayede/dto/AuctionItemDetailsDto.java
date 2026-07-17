package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionItemDetailsDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private boolean isActive;
    private boolean approved;
    private String sellerUsername;
    private String approvedByAdminUsername;
    private List<BidHistoryDto> bidHistory;
}
