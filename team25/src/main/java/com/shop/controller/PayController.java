package com.shop.controller;

import com.shop.domain.Pay;
import com.shop.domain.Product;
import com.shop.domain.User;
import com.shop.service.PayService;
import com.shop.service.ProductService;
import com.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private PayService payService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @GetMapping("/payInsert/{pno}")
    public String payInsert(@PathVariable("pno") long pno, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = userService.findByUserId(userId);
        Product product = productService.getProduct(pno);
        model.addAttribute("product", product);
        model.addAttribute("user", user);

        return "pay/payInsert";
    }

    //결제하기~~
    @PostMapping("/payInsert")
    public String payInsertPro(Pay pay) throws Exception {
        int check = payService.payInsert(pay);
        if (check == 1) {
            log.info("결제 성공");
            return "redirect:/";
        }
        else {
            log.info("결제 실패");
            return "redirect:/";
        }
    }

    //나의 구매 내역
    @GetMapping("/myPayList")
    public String myPayList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId  = authentication.getName();
        System.out.println(userId);
        List<Pay> myPayList = payService.myPayListByUserId(userId);
        model.addAttribute("myPayList",myPayList);
        return "pay/myPayList";
    }

    @GetMapping("/payComplete/{pno}/{payNo}")
    @Transactional
    public String payComplete(@PathVariable("pno") long pno, @PathVariable("payNo") long payNo, HttpSession session) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("num", 1); // 또는 2
        paramMap.put("pno", pno); // pno 값 설정
        productService.updateStatus(paramMap);
        payService.updateShip(4,payNo);
        session.setAttribute("selectedTab", "tab3");

        return "redirect:/member/myProList";
    }

    //나의 판매 내역
    @GetMapping("/mySaleList")
    public String mySaleList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId  = authentication.getName();

        List<Product> mySaleList = productService.findByUserId(userId);

        model.addAttribute("mySaleList",mySaleList);
        return "pay/mySaleList";
    }

    //판매자의 송장 등록
    @GetMapping("/shipInsert/{pno}")
    public String shipInsert(@PathVariable("pno") Long pno, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId  = authentication.getName();
        User user = userService.findByUserId(userId);

        Pay pay = payService.getPay(pno);
        pay.setAddress(pay.getAddr1()+" "+pay.getAddr2());
        model.addAttribute("pay", pay);
        model.addAttribute("user", user);
        return "pay/shipInsert";
    }

    //판매자의 송장 등록~~~~
    @PostMapping("/shipInsert")
    public String shipInsertPro(Pay pay) throws Exception {
        int check = payService.updatePayByPno(pay);
        if (check == 1) {
            log.info("송장 등록 성공");
            return "redirect:/member/myProList";
        }
        else {
            log.info("송장 등록 실패");
            return "redirect:/member/myProList";
        }
    }

    @GetMapping("/completeShip/{pno}")
    public String completeShip(@PathVariable("pno") Long pno){
        Pay pay = payService.getPay(pno);
        payService.updateShip(3,pay.getPayNo());
        return "redirect:/member/myProList";
    }
}

