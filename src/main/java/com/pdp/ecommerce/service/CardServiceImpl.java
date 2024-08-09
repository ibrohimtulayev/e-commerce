package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Card;
import com.pdp.ecommerce.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String cardNUmber) {
        return  cardRepository.findByNumber(cardNUmber);
    }

    public  boolean isCardExpired(String cardExpiryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth cardExpiry = YearMonth.parse(cardExpiryDate, formatter);
            YearMonth currentMonth = YearMonth.now();
            return cardExpiry.isBefore(currentMonth);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use MM/YY.");
            return true;
        }
    }
}
