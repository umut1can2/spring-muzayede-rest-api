package com.example.muzayede.controller;

import com.example.muzayede.dto.BidCreateDto;
import com.example.muzayede.dto.BidHistoryDto;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.service.BidService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {
    private final BidService bidService;

    public BidController(BidService bidService)
    {
        this.bidService = bidService;
    }

    @PostMapping("/bid")
    public Bid MakeBid(@RequestBody BidCreateDto dto)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return bidService.CreateBid(dto, username);
    }
    
    @GetMapping("/auction/{id}/history")
    public List<BidHistoryDto> getBidHistory(@PathVariable Long id)
    {
        return bidService.getBidHistory(id);
    }
}
