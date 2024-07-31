package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Rating;
import com.pdp.ecommerce.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    @Override
    public void save(Rating rating) {
        ratingRepository.save(rating);
    }
}
