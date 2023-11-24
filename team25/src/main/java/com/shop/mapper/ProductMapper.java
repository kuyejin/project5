package com.shop.mapper;

import com.shop.domain.Likes;
import com.shop.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    void save(Product item);
    List<Product> findAll();      //상품 전체
    Product getProduct(Long pno);    //상품 상세
    Product findByPno(Long pno);
    List<Product> findByUserId(String seller);

    Product getLatestproduct();      //최신 상품
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Long pno);

    //소윤 추가 {상품의 판매상황}
    void updateStatus(Map<String, Object> paramMap);

    int checkLiked(Likes proLikes);

    void removeLike(Likes proLikes);

    void addLike(Likes proLikes);

    List<Long> getLikedProductsByUser(String userId);

    List<Likes> getByIdLikeList(String userId);

    //상품별 좋아요 카운트 수 졍렬
    List<Product> orderbyPnoCount();
    void updateProductStatus(String status, Long pno);

    void updateResdate(Long pno);
}
