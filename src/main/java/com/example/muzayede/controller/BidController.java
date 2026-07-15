package com.example.muzayede.controller;

import com.example.muzayede.dto.BidCreateDto;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.service.BidService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return bidService.CreateBid(dto);
    }
}
