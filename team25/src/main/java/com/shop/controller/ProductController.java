package com.shop.controller;

import com.shop.domain.Likes;
import com.shop.domain.Pay;
import com.shop.domain.ChatRoom;
import com.shop.domain.Product;
import com.shop.domain.Review;
import com.shop.service.PayService;
import com.shop.service.ChatService;
import com.shop.service.ProductService;

import com.shop.service.ReviewService;
import com.shop.service.UserService;
import groovy.util.logging.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PayService payService;

    @GetMapping("/productList")
    public String getProductAll(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("product", productList);
        return "product/productList";
    }

    // 상품 상세보기
    @GetMapping("/getProduct/{pno}")
    public String getProduct(@PathVariable("pno") long pno, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();


        //상품 정보
        Product product = productService.getProduct(pno);
        System.out.println(product);

        LocalDateTime resdate = product.getResdate();
        LocalDateTime threeDaysLater = resdate.plus(3, ChronoUnit.DAYS); //3일 지난
        System.out.println(threeDaysLater);

        //판매자 정보
        List<Review> proSellerReview = reviewService.proSellerReview(product.getSeller());
        System.out.println(proSellerReview);


        //찜하기
        List<Long> likedProductIds;

        // 해당 회원이 좋아요한 목록 반환
        likedProductIds = productService.getLikedProductsByUser(userId);
        System.out.println(likedProductIds);


        model.addAttribute("threeDaysLater", threeDaysLater);
        model.addAttribute("likedProductIds", likedProductIds);
        model.addAttribute("proSellerReview", proSellerReview);
        model.addAttribute("userId", userId);
        model.addAttribute("product", product);
        model.addAttribute("proPno", product.getPno());

        return "product/productDetail";
    }


    //상품 끌어올리기
    @GetMapping("/updateResdate/{pno}")
    public String updateResdate(@PathVariable("pno") long pno, Model model) {
        productService.updateResdate(pno);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();


        //상품 정보
        Product product = productService.getProduct(pno);
        System.out.println(product);

        //판매자 정보
        List<Review> proSellerReview = reviewService.proSellerReview(product.getSeller());
        System.out.println(proSellerReview);


        //찜하기
        List<Long> likedProductIds;

        // 해당 회원이 좋아요한 목록 반환
        likedProductIds = productService.getLikedProductsByUser(userId);
        System.out.println(likedProductIds);


        model.addAttribute("likedProductIds", likedProductIds);
        model.addAttribute("proSellerReview", proSellerReview);
        model.addAttribute("userId", userId);
        model.addAttribute("product", product);
        model.addAttribute("proPno", product.getPno());

        return "product/productDetail";
    }


    // 상품 상세 페이지 - 판매자, 구매자 가능
//    @GetMapping("/item/view/{itemId}")
//    public String ItemView(Model model, @PathVariable("itemId") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        if(principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
//            // 판매자
//            User user = principalDetails.getUser();
//
//            model.addAttribute("item", itemService.itemView(id));
//            model.addAttribute("user", user);
//
//            return "itemView";
//        } else {
//            // 구매자
//            User user = principalDetails.getUser();
//
//            // 페이지에 접속한 유저를 찾아야 함
//            User loginUser = userPageService.findUser(user.getId());
//
//            int cartCount = 0;
//            Cart userCart = cartService.findUserCart(loginUser.getId());
//            List<CartItem> cartItems = cartService.allUserCartView(userCart);
//
//            for(CartItem cartItem : cartItems) {
//                cartCount += cartItem.getCount();
//            }
//
//            model.addAttribute("cartCount", cartCount);
//            model.addAttribute("item", itemService.itemView(id));
//            model.addAttribute("user", user);
//
//            return "itemView";
//        }
//    }


    // add form
    @GetMapping("/addProduct")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/addProduct";
    }


    // 상품 등록 처리
    @PostMapping("/addProduct")
    public String addproduct(Product product, MultipartFile[] imgFile, HttpServletRequest req) throws Exception {
        //로그인 후 사용자 정보 가져와서 모델에 추가
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String loginId  = authentication.getName();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        product.setSeller(userId);

        productService.saveProduct(product, imgFile,req);

  /*      if(principalDetails.getRole().getRoleName().equals("TEACHER")) {
            // 판매자
            product.setSeller(principalDetails.getUser());
            productService.saveProduct(product, imgFile);
            return "redirect:product/productLis";
        } else {
            // 일반 회원이면 거절 -> main
            return "redirect:/";
        }*/

        return "redirect:/product/productList";
    }


    @GetMapping("/updateProduct/{pno}")
    public String updateProductForm(@PathVariable("pno") Long pno, Model model) {
        // 사용자의 권한 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN")) ||
                authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("TEACHER"))) {
            System.out.println("안녕");
            //아이디 확인
            String userId = authentication.getName(); // 로그인한 사용자의 아이디를 얻는 방법으로 변경
            String getId = productService.getProduct(pno).getSeller();

            // 상품을 올린 판매자 id와 현재 로그인 중인 판매자의 id가 같아야 수정 가능
            if (getId.equals(userId)) {
                model.addAttribute("product", productService.getProduct(pno));
                return "product/updateProduct";
            } else {
                return "redirect:/";
            }
        } else {
            // 일반 회원이면 거절 -> main
            return "redirect:/";
        }

    }


    // 상품 수정 (POST) - 판매자만 가능
    @PostMapping("/updateProduct/{pno}")
    public String updateProduct(Product product, @PathVariable("pno") Long pno, Model model, MultipartFile[] imgFile) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN")) ||
                    authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("TEACHER"))) {
                //아이디 확인
                String userId = authentication.getName(); // 로그인한 사용자의 아이디를 얻는 방법으로 변경
                String getId = productService.getProduct(pno).getSeller();

                // 상품을 올린 판매자 id와 현재 로그인 중인 판매자의 id가 같아야 수정 가능
                if (getId.equals(userId)) {
                    List<Product> myproList = productService.findByUserId(getId);
                    productService.updateProduct(product, pno, imgFile);
                    model.addAttribute("myproList", myproList);
                    return "member/myProductList";
                } else {
                    return "redirect:/";
                }
            } else {
                // 일반 회원이면 거절 -> main
                return "redirect:/";
            }
        } else {
            // 로그인한 사용자가 없는 경우
            return "redirect:/";
        }
    }

    // 상품 삭제 - 판매자만 가능
    @GetMapping("/deleteProduct/{pno}")
    public String productDelete(@PathVariable("pno") Long pno, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN")) ||
                    authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("TEACHER"))) {
                //아이디 확인
                String userId = authentication.getName(); // 로그인한 사용자의 아이디를 얻는 방법으로 변경
                String getId = productService.getProduct(pno).getSeller();

                // 상품을 올린 판매자 id와 현재 로그인 중인 판매자의 id가 같아야 수정 가능
                if (getId.equals(userId)) {

                    productService.deleteProduct(pno);
                    List<Product> myproList = productService.findByUserId(getId);
                    model.addAttribute("myproList", myproList);

                    return "member/myProductList";
                } else {
                    return "redirect:/";
                }
            } else {
                // 일반 회원이면 거절 -> main
                return "redirect:/";
            }
        } else {
            // 로그인한 사용자가 없는 경우
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/likeProduct", method = RequestMethod.GET)
    public String likeProduct(Model model, @RequestParam("pno") String pno, @RequestParam("userId") String userId) {
        return "redirect:/product/getProduct?pno=" + pno;
    }

    // 좋아요 목록
    @GetMapping("/likeList/{pno}")
    public String LikeList(@PathVariable("pno") Long pno, HttpServletRequest req, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<Likes> proLikes = productService.getByIdLikeList(userId);
        List<Product> proList = new ArrayList<>();

        for (Likes pro : proLikes) {
            proList.add(productService.getProduct(pro.getPno()));
        }

        model.addAttribute("lectureList", proList);
        model.addAttribute("likeList", proLikes);
        return "product/likeList";
    }

    // 강의 좋아요

    @PostMapping("/productLike")
    public void productLike(@RequestParam("pno") Long pno, @RequestParam("userId") String userId, HttpServletResponse res, HttpServletRequest req, Model model) throws Exception {

        //String userId = req.getParameter("userId");
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String userId = authentication.getName();

        Likes proLikes = new Likes();
        proLikes.setUserId(userId);
        proLikes.setPno(pno);


        try {
            if (productService.checkLiked(proLikes) > 0) {
                productService.removeLike(proLikes);
                res.getWriter().write("unliked");
                req.setAttribute("isLiked", false);
            } else {
                productService.addLike(proLikes);
                res.getWriter().write("liked");
                req.setAttribute("isLiked", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().write("error");
        }
    }


    // 좋아요 삭제
    @GetMapping("/delLike/{pno}")
    public String delLike(@PathVariable("pno") Long pno, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        //상품 관리
        List<Product> myproList = productService.findByUserId(userId);
        System.out.println(myproList);
        model.addAttribute("myproList", myproList);


        //소윤의 구매내역
        List<Pay> myPayList = payService.myPayListByUserId(userId);
        model.addAttribute("myPayList", myPayList);

        // 내가 쓴 후기
        List<Review> proReview = reviewService.proReview(userId);
        System.out.println(proReview);
        model.addAttribute("proReview", proReview);

        //내가 받은 후기
        List<Review> proSellerReview = reviewService.proSellerReview(userId);
        System.out.println(proSellerReview);
        model.addAttribute("proSellerReview", proSellerReview);


        //좋아요
        //pno, userId
        List<Likes> proLikes = productService.getByIdLikeList(userId);
        List<Product> proList = new ArrayList<>();
        for (Likes pro : proLikes) {
            System.out.println(pro);
            proList.add(productService.getProduct(pro.getPno()));
        }

        System.out.println(proList);
        model.addAttribute("proLikes", proLikes);
        model.addAttribute("proList", proList);

        Likes proLikes2 = new Likes();
        proLikes2.setUserId(userId);
        proLikes2.setPno(pno);
        productService.removeLike(proLikes2);
        return "member/myProductList";
    }


    @RequestMapping("/updateStatus")
    @ResponseBody
    public String updateStatus(@RequestParam String status, @RequestParam Long pno) {
        // productService를 사용하여 DB 업데이트 수행
        System.out.println("pno "+pno+" status"+status);
        productService.updateProductStatus(status, pno);
        return "redirect:/member/myProductList";  // 또는 다른 성공 메시지
    }
}