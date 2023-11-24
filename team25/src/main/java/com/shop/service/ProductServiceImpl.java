package com.shop.service;

import com.shop.domain.Likes;
import com.shop.domain.Product;
import com.shop.domain.ProductFile;
import com.shop.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;


    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    @Override
    public Product getProduct(Long pno) {
        return productMapper.getProduct(pno);
    }

    @Override
    public List<Product> findByUserId(String seller) {
        return productMapper.findByUserId(seller);
    }


    @Override
    public void save(Product product) {
        productMapper.save(product);
    }


    public void saveProduct(Product product, MultipartFile[] imgFiles, HttpServletRequest req) throws IOException {
        // static은 정적폴더라서 늦게 업로드됨
        //String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload/";

        //개발
        //String projectPath = "D:/team25_upload/";  //학원
        // String projectPath = "C:/team25_upload/";    //예진집
        String uploadSev = System.getProperty("user.dir") + "/src/main/webapp/upload/";



        //운영
        //String uploadSev = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\team25\\upload";


                                        //application.yml location 적용시 폴더

        for (int i = 0; i < imgFiles.length; i++) {
            MultipartFile imgFile = imgFiles[i];
            String oriImgName = imgFile.getOriginalFilename();
            String imgName = "";

            // UUID 를 이용하여 파일명 새로 생성
            UUID uuid = UUID.randomUUID();
            String savedFileName = uuid + "_" + oriImgName;
            
            
            imgName = savedFileName;
            //개발
            //File saveFile = new File(projectPath, imgName);

            //운영
            File saveFile = new File(uploadSev, imgName);
            imgFile.transferTo(saveFile);

            // 각 이미지에 대한 처리 (imgsrc1, imgsrc2, imgsrc3, imgsrc4)
            switch (i + 1) {
                case 1:
                    product.setImgsrc1(imgName);
                    break;
                case 2:
                    product.setImgsrc2(imgName);
                    break;
                case 3:
                    product.setImgsrc3(imgName);
                    break;
                case 4:
                    product.setImgsrc4(imgName);
                    break;
                // 추가적인 이미지 필요에 따라 계속해서 확장 가능
                // ...
            }
        }

        productMapper.save(product);
    }


    @Override
    public void addProduct(Product product) {
        productMapper.addProduct(product);
    }

    @Override
    @Transactional
    public void updateProduct(Product product, Long pno, MultipartFile[] imgFiles) throws IOException {
        Product update = productMapper.findByPno(pno);
        update.getPno();
        update.setCateno(product.getCateno());
        update.setPname(product.getPname());
        update.setPcomment(product.getPcomment());
        update.setPrice(product.getPrice());
        update.setQuantity(product.getQuantity());
        update.setQuality(product.getQuality());

        //파일 처리
        //개발
       // String projectPath = "D:/team25_upload/";  //학원
//        String projectPath = "C:/team25_upload/";    //예진집
        String uploadSev = System.getProperty("user.dir") + "/src/main/webapp/upload/";

       //운영
        //String uploadSev = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\team25\\upload";

        for (int i = 0; i < imgFiles.length; i++) {
            MultipartFile imgFile = imgFiles[i];
            String oriImgName = imgFile.getOriginalFilename();
            String imgName = "";

            // UUID 를 이용하여 파일명 새로 생성
            UUID uuid = UUID.randomUUID();
            String savedFileName = uuid + "_" + oriImgName;
            imgName = savedFileName;

            File saveFile = new File(uploadSev, imgName);
            imgFile.transferTo(saveFile);

            // 각 이미지에 대한 처리 (imgsrc1, imgsrc2, imgsrc3, imgsrc4)
            switch (i + 1) {
                case 1:
                    update.setImgsrc1(imgName);
                    break;
                case 2:
                    update.setImgsrc2(imgName);
                    break;
                case 3:
                    update.setImgsrc3(imgName);
                    break;
                case 4:
                    update.setImgsrc4(imgName);
                    break;
                // 추가적인 이미지 필요에 따라 계속해서 확장 가능
                // ...
            }
        }
        productMapper.updateProduct(update);
    }


    @Override
    public void deleteProduct(Long pno) {
        productMapper.deleteProduct(pno);
    }

    @Override
    public void setproductFile(ProductFile productFile) {

    }


    @Override
    public Product getLatestproduct() {
        return productMapper.getLatestproduct();
    }





    @Override
    public void updateStatus(Map<String, Object> paramMap) { productMapper.updateStatus(paramMap);
    }

    @Override
    public int checkLiked(Likes proLikes) {
        return productMapper.checkLiked(proLikes);
    }


    @Override
    public void removeLike(Likes proLikes) {
        productMapper.removeLike(proLikes);
    }

    @Override
    public void addLike(Likes proLikes) {
        productMapper.addLike(proLikes);
    }

    // 좋아요 누른 상품의 id 목록 반환
    @Override
    public List<Long> getLikedProductsByUser(String userId) {
        return productMapper.getLikedProductsByUser(userId);
    }


    // 유저의 좋아요 목록 출력
    @Override
    public List<Likes> getByIdLikeList(String userId) {return productMapper.getByIdLikeList(userId);}

    @Override
    public List<Product> orderbyPnoCount() {
        return productMapper.orderbyPnoCount();
    }

    @Override
    public void updateProductStatus(String status, Long pno) {
        productMapper.updateProductStatus(status, pno);
    }
    public void updateResdate(Long pno) {
        productMapper.updateResdate(pno);
    }

}
