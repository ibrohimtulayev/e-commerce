package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Rating;
import com.pdp.ecommerce.entity.User;
import org.springframework.stereotype.Service;

@Service

public interface RatingService {
    void save(Rating rating);

    Rating findByUser(User user);
}
