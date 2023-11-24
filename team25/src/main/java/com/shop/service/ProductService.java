package com.shop.service;

import com.shop.domain.Likes;
import com.shop.domain.Product;
import com.shop.domain.ProductFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    public void save(Product product);
    public List<Product> findAll();      //상품 전체
    public Product getProduct(Long pno);    //상품 상세
    public List<Product> findByUserId(String seller);
    public Product getLatestproduct();      //최신 상품



    public void addProduct(Product product);
    public void updateProduct(Product product, Long pno, MultipartFile[] imgFile) throws IOException;
    public void deleteProduct(Long pno);


    public void setproductFile(ProductFile productFile);


    public void saveProduct(Product product, MultipartFile[] imgFiles, HttpServletRequest req) throws IOException;
    public void updateStatus(Map<String, Object> paramMap);


    //좋아요
    public int checkLiked(Likes proLikes);

    public void removeLike(Likes proLikes);

    public void addLike(Likes proLikes);

    public List<Long> getLikedProductsByUser(String userId);

    // 유저의 좋아요 목록 출력
    public List<Likes> getByIdLikeList(String userId);

    //상품별 좋아요 카운트 수 졍렬
    public List<Product> orderbyPnoCount();
    public void updateProductStatus(String status, Long pno);

    public void updateResdate(Long pno);
}
