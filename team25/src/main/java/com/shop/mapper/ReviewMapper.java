package com.shop.mapper;

import com.shop.domain.Product;
import com.shop.domain.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {


    void insertReview(Review review);

    Review getProReview(Review review);

    List<Review> proReview(String userId);

    List<Review> proSellerReview(String seller);

    int reviewCheck(Review review);
}
