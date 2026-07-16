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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tools.jackson.databind.util.BeanUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    /* Autowired yerine bu oneriliyor. */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BidRepository bidRepository;
    private final AuctionItemRepository auctionItemRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, BidRepository bidRepository, AuctionItemRepository auctionItemRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.bidRepository = bidRepository;
        this.auctionItemRepository = auctionItemRepository;
    }

    public User CreateUser(UserCreateDto user)
    {
        User newUser = new User();

        BeanUtils.copyProperties(user, newUser);
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setBlockedBalance(BigDecimal.ZERO);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    public void addBalance(Long userId, BigDecimal amount)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(userId.toString() +
                        " id'li Kullanici bulunamadi!"));

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto)
    {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username or PASSWORD are wrong!"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Username or PASSWORD are wrong!");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new LoginResponseDto(token, user.getUsername());
    }

    public UserProfileDto getUserProfile(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profil bulunamadi!"));

        List<Bid> activeBids = bidRepository.findActiveBidsByUser(user);

        List<UserActiveBidsDto> activeBidsDtoList = new ArrayList<>();

        for(Bid b : activeBids)
        {
            UserActiveBidsDto dto = new UserActiveBidsDto();
            dto.setAuctionItemId(b.getId());
            dto.setCurrentPrice(b.getAuctionItem().getCurrentPrice());
            dto.setEndTime(b.getAuctionItem().getEndTime());
            dto.setMyBid(b.getBidAmount());
            dto.setAuctionItemTitle(b.getAuctionItem().getTitle());
            activeBidsDtoList.add(dto);
        }

        // kendi actigi ilanlar
        List<AuctionItem> auctionItems = auctionItemRepository.findBySeller(user);
        List<MyAuctionDto> myAuctionDtos = new ArrayList<>();

        for(AuctionItem i : auctionItems)
        {
            MyAuctionDto myAuctionDto = new MyAuctionDto();
            myAuctionDto.setApproved(i.isApproved());
            myAuctionDto.setTitle(i.getTitle());
            myAuctionDto.setId(i.getId());
            myAuctionDto.setActive(i.isActive());
            myAuctionDto.setEndTime(i.getEndTime());
            myAuctionDto.setCurrentPrice(i.getCurrentPrice());
            myAuctionDto.setStartPrice(i.getStartPrice());
            myAuctionDtos.add(myAuctionDto);
        }

        // daha onceden kazandigi ilanlar
        List<AuctionItem> wonAuctions = auctionItemRepository.findWonAuctionsByUser(user);
        List<WonAuctionsDto> wonAuctionsDtos = new ArrayList<>();

        for(AuctionItem i : wonAuctions)
        {
            WonAuctionsDto wonAuctionsDto = new WonAuctionsDto();

            wonAuctionsDto.setId(i.getId());
            wonAuctionsDto.setTitle(i.getTitle());
            wonAuctionsDto.setFinalPrice(i.getCurrentPrice());
            wonAuctionsDto.setEndTime(i.getEndTime());
            wonAuctionsDto.setSellerUsername(i.getSeller().getUsername());

            wonAuctionsDtos.add(wonAuctionsDto);
        }

        UserProfileDto userDto = new UserProfileDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUsername());
        userDto.setActiveBidsDtoList(activeBidsDtoList);
        userDto.setEMail(user.getEmail());
        userDto.setBalance(user.getBalance());
        userDto.setBlockedBalance(user.getBlockedBalance());
        userDto.setMyAuctionDtos(myAuctionDtos);
        userDto.setWonAuctionsDtos(wonAuctionsDtos);

        return userDto;
    }
}
