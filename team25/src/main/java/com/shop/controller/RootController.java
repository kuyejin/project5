package com.shop.controller;

import com.shop.domain.Likes;
import com.shop.domain.Product;
import com.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RootController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {

        // 최신순
        List<Product> productList = productService.findAll();
        model.addAttribute("product", productList);

        // 인기순(찜순서)
        List<Product> PnoCountList = productService.orderbyPnoCount();
        model.addAttribute("PnoCountList", PnoCountList);
        System.out.println(PnoCountList);


        return "index";
    }

}
