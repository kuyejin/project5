package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK, LEAVE, NOTICE
    }

    private Long chatNo;
    @NotNull
    private MessageType type;           // 메시지 타입
    @NotNull
    private Long roomNo;                // 방 번호
    @NotNull
    private String sender;              // 채팅을 보낸 사람
    private String receiver;            // 채팅을 받는 사람
    @NotNull
    private String message;             // 메시지
    private String status = "UNREAD";   // 읽음 여부
    private String time;                // 채팅 발송 시간
}
