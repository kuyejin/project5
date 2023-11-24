package com.shop.service;

import com.shop.domain.Product;
import com.shop.domain.ProductFile;
import com.shop.domain.Review;
import com.shop.mapper.ProductMapper;
import com.shop.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public void insertReview(Review review) {
        reviewMapper.insertReview(review);
    }

    @Override
    public Review getProReview(Review review) {
        return reviewMapper.getProReview(review);
    }

    @Override
    public List<Review> proReview(String userId) {
        return reviewMapper.proReview(userId);
    }

    @Override
    public List<Review> proSellerReview(String seller) {
        return reviewMapper.proSellerReview(seller);
    }

    @Override
    public int reviewCheck(Review review) {
        return reviewMapper.reviewCheck(review);
    }
}
