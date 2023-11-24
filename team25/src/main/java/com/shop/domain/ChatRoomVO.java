package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomVO {
    private Long roomNo;             // 채팅방 고유번호
    private String buyer;            // 구매 희망자
    private String buyerNm = "";
    private Long pno;                // 상품 고유번호
    private String status = "ON";    // 채팅방 차단을 염두에 두었다.
    private String seller;          // 판매자
    private String sellerNm = "";

}
