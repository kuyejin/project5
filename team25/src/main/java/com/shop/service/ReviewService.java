package com.shop.service;

import com.shop.domain.Product;
import com.shop.domain.ProductFile;
import com.shop.domain.Review;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReviewService {


    public void insertReview(Review review);

    public Review getProReview(Review review);

    // 내가 작성한 후기
    public List<Review> proReview(String userId);

    // 내가 받은 후기
    public List<Review> proSellerReview(String seller);


    public int reviewCheck(Review review);
}

