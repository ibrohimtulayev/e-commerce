package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Comment;
import org.springframework.stereotype.Service;

@Service

public interface CommentService {
    void save(Comment comment1);
}
