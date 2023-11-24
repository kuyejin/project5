package com.shop.controller;

import com.shop.domain.News;
import com.shop.service.ChatService;
import com.shop.service.ProductService;
import com.shop.util.Crawler;
import groovy.util.logging.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/news")
public class NewsController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String newsCrawler(HttpServletRequest request, Model model) throws Exception {
        List<News> newsList = Crawler.naverNewsKeyword("천재교육", 5);
        model.addAttribute("newsList", newsList);
        return "comunity/news";
    }
}