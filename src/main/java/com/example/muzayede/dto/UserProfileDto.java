package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String userName;
    private String eMail;
    private BigDecimal balance;
    private BigDecimal blockedBalance;
    private List<UserActiveBidsDto> activeBidsDtoList; // teklif yaptigi ilanlar
    private List<MyAuctionDto> myAuctionDtos; // kendi actigi ilanlar
    private List<WonAuctionsDto> wonAuctionsDtos; // kazandigi ilanlar
}
