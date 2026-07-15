package com.example.muzayede.service;

import com.example.muzayede.dto.AuctionItemCreateDto;
import com.example.muzayede.dto.AuctionListItemDto;
import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.User;
import com.example.muzayede.exception.ResourceNotFoundException;
import com.example.muzayede.repository.AuctionItemRepository;
import com.example.muzayede.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import tools.jackson.databind.util.BeanUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;

    public AuctionItemService(AuctionItemRepository auctionItemRepository
    , UserRepository userRepository)
    {
        this.auctionItemRepository = auctionItemRepository;
        this.userRepository = userRepository;
    }

    public AuctionItem createAuctionItem(AuctionItemCreateDto item)
    {
        User seller = userRepository.findById(item.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici buluanamadi."));

        AuctionItem newItem = new AuctionItem();

        BeanUtils.copyProperties(item, newItem);
        newItem.setCurrentPrice(item.getStartPrice());
        newItem.setActive(true);

        // bitis tarihinin hesaplanmasi
        newItem.setEndTime(LocalDateTime.now().plusDays(item.getDurationInDays()));

        newItem.setSeller(seller);

        return auctionItemRepository.save(newItem);
    }

    public void DeleteAuctionItem(Long id)
    {
        AuctionItem item = auctionItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("We didn't find The item which you want to delete it!")
        );

        auctionItemRepository.delete(item);
    }

    public List<AuctionListItemDto> list()
    {
        List<AuctionItem> aItem = auctionItemRepository.findByIsActiveTrueAndEndTimeAfter(LocalDateTime.now());

        List<AuctionListItemDto> items = new ArrayList<AuctionListItemDto>();

        for(AuctionItem item : aItem)
        {
            items.add(new AuctionListItemDto(item.getTitle(),item.getDescription(),item.getCurrentPrice(),item.getEndTime(), item.getSeller().getUsername()));
        }
        return items;
    }

}
