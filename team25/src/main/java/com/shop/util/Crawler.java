package com.shop.util;


import com.shop.domain.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    public static List<News> naverNewsKeyword(String keyword, int cnt) throws Exception{
        List<News> list = new ArrayList<>();
        String url = "https://search.naver.com/search.naver?where=news&ie=utf8&sm=nws_hty&query="+keyword;

        Connection conn = Jsoup.connect(url)
                .header("Content-Type", "application/json;charset=UTF-8")
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .ignoreContentType(true);

        Document doc = conn.get();
        Elements titles = doc.select(".news_wrap .news_tit");
        Elements imgs = doc.select(".news_wrap > a > img");
        Elements contents = doc.select(".news_wrap .dsc_wrap .dsc_txt_wrap");
        Elements hrefs = doc.select("div.news_wrap.api_ani_send > div > a");

        for(int i=0; i<cnt; i++){
            String title = titles.get(i).text();
            String img = imgs.get(i).attr("data-lazysrc");
            String content = contents.get(i).text();
            String href = hrefs.get(i).attr("href");
            News dto = new News();
            dto.setTitle(title);
            dto.setImg(img);
            dto.setContent(content);
            dto.setHref(href);
            list.add(dto);
        }

        return list;
    }
}