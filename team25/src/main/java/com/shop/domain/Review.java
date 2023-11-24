package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {
    private int no;
    private String id;
    private String content;
    private String resdate;
    private int score;
    private Long pno;


    private String pname;
    private String imgsrc1;
    private String seller;
    private int check;


}