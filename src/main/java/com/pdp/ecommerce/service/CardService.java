package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Card;
import org.springframework.stereotype.Service;

@Service

public interface CardService {
    void save(Card card);
}
