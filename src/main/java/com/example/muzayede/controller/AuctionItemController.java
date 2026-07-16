package com.example.muzayede.controller;

import com.example.muzayede.config.Security;
import com.example.muzayede.dto.AuctionItemCreateDto;
import com.example.muzayede.dto.AuctionListItemDto;
import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.service.AuctionItemService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;

    public AuctionItemController(AuctionItemService auctionItemService)
    {
        this.auctionItemService = auctionItemService;
    }

    @PostMapping("/create")
    public AuctionItem CreateAuctionItem(@RequestBody AuctionItemCreateDto dto)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return auctionItemService.createAuctionItem(dto, userName);
    }

    @PostMapping("/delete/{id}")
    public void DeleteAuctionItem(@PathVariable Long id)
    {
        auctionItemService.DeleteAuctionItem(id);
    }

    @GetMapping("/list")
    public List<AuctionListItemDto> list()
    {
        return auctionItemService.list();
    }

    @PostMapping("/{id}/approve")
    public void ApproveAuctionItem(@PathVariable Long id)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auctionItemService.approveAuctionItem(id, username);
    }

}
