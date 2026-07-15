package com.example.muzayede.service;

import com.example.muzayede.dto.BidCreateDto;
import com.example.muzayede.entity.AuctionItem;
import com.example.muzayede.entity.Bid;
import com.example.muzayede.entity.User;
import com.example.muzayede.exception.InsufficentBalanceException;
import com.example.muzayede.exception.ResourceNotFoundException;
import com.example.muzayede.repository.AuctionItemRepository;
import com.example.muzayede.repository.BidRepository;
import com.example.muzayede.repository.UserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionItemRepository auctionItemRepository;

    public BidService(
            BidRepository bidRepository,
            UserRepository userRepository,
            AuctionItemRepository auctionItemRepository
    )
    {
        this.bidRepository = bidRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.userRepository = userRepository;
    }

    public Bid CreateBid(BidCreateDto dto)
    {
        User bidder = userRepository.findById(dto.getBidderId())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi."));

        AuctionItem item = auctionItemRepository.findById(dto.getAuctionItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item bulunamadi."));

        if(item.isActive() == false || item.getEndTime().isBefore(LocalDateTime.now()))
        {
            throw new ResourceNotFoundException("Aktif olmayan veya muzayede suresi bitmis bir ilana teklif sunulamaz!");
        }

        /* kendi urunue teklif etmemesinin kontrolu */
        if(item.getSeller().getId() == bidder.getId())
        {
            throw new RuntimeException("Kendi urununuze teklif veremezsiniz!");
        }

        /* verilen teklifin fiyatinin daha yuksek olmasi gerekir */
        if(dto.getBidAmount().compareTo(item.getCurrentPrice()) <= 0)
        {
            throw new RuntimeException("Teklif " + item.getCurrentPrice().toString() + " miktarindan daha buyuk olmali!");
        }

        /*
        *
        * */
        Bid highestBid = bidRepository.findHighestBidForItem(item).orElse(null);

        // eger kendi fiyatini arttiriyorsa
        // daha sonra buraya cekilmek istiyor musunuz falan yazilacak
        // daha yuksek bir fiyat verilirse mesaj gidecek fln o yuzden
        if(highestBid != null && highestBid.getBidder().getId() == (bidder.getId()))
        {
            BigDecimal alreadyBlocked = highestBid.getBidAmount();

            BigDecimal fark = dto.getBidAmount().subtract(alreadyBlocked);

            if(bidder.getBalance().compareTo(fark) < 0)
            {
                throw new InsufficentBalanceException("Yetersiz bakiye!!, Daha onceki teklifinizin uzerine.");
            }

            bidder.setBalance(bidder.getBalance().subtract(fark));
            bidder.setBlockedBalance(bidder.getBlockedBalance().add(fark));
            userRepository.save(bidder);
        }
        else
        {
            if(bidder.getBalance().compareTo(dto.getBidAmount()) < 0)
            {
                throw new InsufficentBalanceException("Yetersiz Bakiye!");
            }

            bidder.setBalance(bidder.getBalance().subtract(dto.getBidAmount()));
            bidder.setBlockedBalance(bidder.getBlockedBalance().add(dto.getBidAmount()));
            userRepository.save(bidder);

            // daha once teklif yapan biri varsa eleniyor burada
            if(highestBid != null)
            {
                User oldBidder = highestBid.getBidder();
                BigDecimal oldBidAmount = highestBid.getBidAmount();

                oldBidder.setBlockedBalance(oldBidder.getBlockedBalance().subtract(oldBidAmount));
                oldBidder.setBalance(oldBidder.getBalance().add(oldBidAmount));
                userRepository.save(oldBidder);
            }
        }

        /* tum kontroller bitti ve basarili */

        // yeni fiyatin veritabaniuna kaydedilmesi
        item.setCurrentPrice(dto.getBidAmount());
        auctionItemRepository.save(item);

        Bid newBid = new Bid();
        newBid.setBidAmount(dto.getBidAmount());
        newBid.setBidTime(LocalDateTime.now());
        newBid.setBidder(bidder);
        newBid.setAuctionItem(item);

        return bidRepository.save(newBid);

    }
}
