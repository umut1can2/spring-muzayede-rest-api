package com.example.muzayede.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionItemUpdateDto {
    private String title;
    private String description;
}
