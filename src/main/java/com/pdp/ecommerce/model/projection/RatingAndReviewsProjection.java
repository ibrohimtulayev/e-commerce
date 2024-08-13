package com.pdp.ecommerce.model.projection;

import com.pdp.ecommerce.model.dto.CommentDto;

import java.util.List;

public interface RatingAndReviewsProjection {
    String getUsername();
    Integer getRating();
    List<CommentDto> getComments();
}
