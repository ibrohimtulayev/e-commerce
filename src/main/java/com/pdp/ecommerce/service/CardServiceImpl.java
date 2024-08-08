package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Card;
import com.pdp.ecommerce.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
