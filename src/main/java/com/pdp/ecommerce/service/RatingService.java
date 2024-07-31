package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Rating;
import org.springframework.stereotype.Service;

@Service

public interface RatingService {
    void save(Rating rating);

}
