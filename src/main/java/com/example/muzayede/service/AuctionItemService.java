package com.example.muzayede.service;

import com.example.muzayede.dto.*;
import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.entity.User;
import com.example.muzayede.exception.ResourceNotFoundException;
import com.example.muzayede.repository.AuctionItemRepository;
import com.example.muzayede.repository.BidRepository;
import com.example.muzayede.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.parameters.P;
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
    private final BidRepository bidRepository;

    public AuctionItemService(AuctionItemRepository auctionItemRepository
    , UserRepository userRepository, BidRepository bidRepository)
    {
        this.auctionItemRepository = auctionItemRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    public AuctionItem createAuctionItem(AuctionItemCreateDto item, String userName)
    {
        User seller = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici buluanamadi."));

        AuctionItem newItem = new AuctionItem();

        BeanUtils.copyProperties(item, newItem);
        newItem.setCurrentPrice(item.getStartPrice());
        newItem.setActive(false);
        newItem.setApproved(false);

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
            if(item.isActive())
            {
                items.add(new AuctionListItemDto(item.getTitle(),item.getDescription(),item.getCurrentPrice(),item.getEndTime(), item.getSeller().getUsername()));
            }
        }
        return items;
    }

    public void approveAuctionItem(Long itemId, String adminUserName)
    {
        AuctionItem item = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Aranan urun bulunamadi!"));

        if(item.isApproved())
        {
            throw new RuntimeException("Ilan daha onceden onaylanmis!");
        }

        User approvedAdmin = userRepository.findByUsername(adminUserName)
                        .orElseThrow(() -> new ResourceNotFoundException("Onaylayan admin bulunamadi!"));

        item.setApproved(true);
        item.setActive(true);
        item.setApprovedBy(approvedAdmin);

        auctionItemRepository.save(item);
    }

    public void UpdateAuctionItem(Long itemId, String username, AuctionItemUpdateDto dto)
    {
        AuctionItem item = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Secilen ilan bulunamadi!"));

        if(!item.getSeller().getUsername().equals(username))
            throw new RuntimeException("Bu ilani duzenleme yetkiniz yok!");

        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());

        auctionItemRepository.save(item);
    }

    public void deActivateAuctionItem(Long itemId, String username)
    {
        AuctionItem item = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Aranan ilan bulunamadi!"));

        if(!item.getSeller().getUsername().equals(username))
            throw new RuntimeException("Bu ilani duzenleme yetkiniz yok!");

        item.setActive(false);
        auctionItemRepository.save(item);
    }

    public AuctionItemDetailsDto getAuctionItemDetails(Long id)
    {
        AuctionItem item = auctionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aranan ilan bulunamadi!"));

        List<Bid> bids = bidRepository.findByAuctionItemOrderByBidAmountDesc(item);

        List<BidHistoryDto> historyDtos = new ArrayList<>();

        for(Bid i : bids)
        {
            BidHistoryDto nBid = new BidHistoryDto();
            nBid.setBidAmount(i.getBidAmount());
            nBid.setBidderUserName(i.getBidder().getUsername());
            nBid.setBidTime(i.getBidTime());

            historyDtos.add(nBid);
        }

        AuctionItemDetailsDto newDto = new AuctionItemDetailsDto();
        BeanUtils.copyProperties(item, newDto);
        newDto.setApprovedByAdminUsername(item.getApprovedBy() != null ? item.getApprovedBy().getUsername() : "Henüz Onaylanmadı");        newDto.setBidHistory(historyDtos);
        newDto.setSellerUsername(item.getSeller().getUsername());

        return newDto;
    }
}
