package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pay {
    private Long payNo; //결제 코드
    private String userId; //구매자
    private Long pno; //구매한 상품
    private int price; // 결제 가격

    //배송 관련
    private String userName; //구매자명
    private String tel; //전화번호
    private String email; //이메일
    private String addr1;
    private String addr2;
    private String address; //주소
    private int ship; //배송 현황  -  1: 배송전 2: 배송 중 3: 배송완료
    private String scode; //운송장 정보
    private String sname; //회사 정보

    private String resdate; //구매일
    private String postcode;

    private String pname;
    private String status;    // 판매 중 / 예약 중 / 판매완료
    private String imgsrc1;

    private int check;
}
