package com.shop.controller;


import com.shop.domain.Likes;
import com.shop.domain.Pay;
import com.shop.domain.Product;
import com.shop.domain.Review;
import com.shop.service.PayService;
import com.shop.service.ProductService;
import com.shop.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PayService payService;


    //리뷰 폼 로딩
    @GetMapping("/addReviewForm/{pno}")
    public String addReviewForm(@PathVariable("pno") Long pno, Model model) {

        Product product = productService.getProduct(pno);
        model.addAttribute("product",product);

        return "product/addReview";
    }


    @PostMapping("/addReview/{pno}")
    public String addReview(@PathVariable("pno") Long pno, Model model, Review review, HttpServletRequest req, HttpServletResponse res) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId  = authentication.getName();

        review.setId(userId);
        review.setPno(pno);
        reviewService.insertReview(review);


        List<Product> myproList = productService.findByUserId(userId);
        System.out.println(myproList);
        model.addAttribute("myproList", myproList);

        //소윤의 구매내역
        List<Pay> myPayList = payService.myPayListByUserId(userId);
        //리뷰 체크
        for(Pay re: myPayList){
            review.setPno(re.getPno());
            review.setId(userId);
            int check = reviewService.reviewCheck(review);

            System.out.println("check: " + check );
            re.setCheck(check);
        }
        model.addAttribute("myPayList",myPayList);



        // 내가 쓴 후기
        List<Review> proReview= reviewService.proReview(userId);
        System.out.println(proReview);
        model.addAttribute("proReview", proReview);



        //내가 받은 후기
        List<Review> proSellerReview= reviewService.proSellerReview(userId);
        System.out.println(proSellerReview);
        model.addAttribute("proSellerReview", proSellerReview);


        //좋아요
        //pno, userId
        List<Likes> proLikes = productService.getByIdLikeList(userId);
        List<Product> proList = new ArrayList<>();
        for (Likes pro: proLikes) {
            System.out.println(pro);
            proList.add(productService.getProduct(pro.getPno()));
        }

        System.out.println(proList);
        model.addAttribute("proLikes", proLikes);
        model.addAttribute("proList", proList);

        return "member/myProductList";
    }

    //거래한 상품 리뷰 보기 (select one)
    @GetMapping("/getProReview/{pno}")
    public String getProReview(@PathVariable("pno") Long pno, Model model, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();


        Review review = new Review();
        review.setId(userId);
        review.setPno(pno);


        List<Pay> myPayList = payService.myPayListByUserId(userId);
        Product product = productService.getProduct(pno);
        Review rev =reviewService.getProReview(review);
        System.out.println(rev);

        model.addAttribute("product",product);
        model.addAttribute("review", rev);
        model.addAttribute("myPayList",myPayList);

        //return "redirect:/lecture/getLecture?no=" + review.getPar();
        return "product/getProReview";
    }


    //내가 쓴 후기 보기
//    @GetMapping("proReview/{pno}")
//    public String proReview(@PathVariable("pno") Long pno, Model model, HttpServletRequest req, HttpServletResponse res) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//
//        System.out.println(userId);
//
//        List<Review> reviewList = reviewService.proReview(userId);
//        System.out.println(reviewList);
//        model.addAttribute("reviewList", reviewList);
//
//        return "member/myProductList";
//    }



}
