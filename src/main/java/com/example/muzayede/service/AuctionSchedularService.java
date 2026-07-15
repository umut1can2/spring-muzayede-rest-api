package com.example.muzayede.service;

import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.entity.User;
import com.example.muzayede.repository.AuctionItemRepository;
import com.example.muzayede.repository.BidRepository;
import com.example.muzayede.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionSchedularService {

    private final AuctionItemRepository auctionItemRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public AuctionSchedularService(
            AuctionItemRepository auctionItemRepository,
            BidRepository bidRepository,
            UserRepository userRepository)
    {
        this.auctionItemRepository = auctionItemRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
    }

    // 10 saniye
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void closeExpiredAuctions()
    {
        LocalDateTime now = LocalDateTime.now();

        List<AuctionItem> expiredItems = auctionItemRepository.findByIsActiveTrueAndEndTimeBefore(now);

        if(expiredItems.isEmpty()) return;

        // loglama kismina cevir
        System.out.println("Suresi dolan itemler: " + expiredItems.size() + " adet ilan.");

        for(AuctionItem a : expiredItems)
        {
            a.setActive(false);

            Bid winnerBid = bidRepository.findHighestBidForItem(a).orElse(null);

            if(winnerBid != null)
            {
                User winnerUser = winnerBid.getBidder();
                User seller = a.getSeller();

                BigDecimal finalPrice = winnerBid.getBidAmount();
                System.out.println(
                        a.getTitle() + " urunu icin kazanan " + winnerUser.getUsername()
                        + " odenecek miktar: " + finalPrice + "."
                );

                // paranin duzenlenmesi
                winnerUser.setBlockedBalance(winnerUser.getBlockedBalance().subtract(finalPrice));

                seller.setBalance(seller.getBalance().add(finalPrice));

                userRepository.save(seller);
                userRepository.save(winnerUser);
            }
            else
            {
                System.out.println(a.getTitle() + " urunune hic teklif gelmedi. Muzayede bitiriliyor.");
            }
            auctionItemRepository.save(a);
        }
    }
}
