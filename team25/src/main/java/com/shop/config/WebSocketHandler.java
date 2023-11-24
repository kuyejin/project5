package com.shop.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class WebSocketHandler extends AbstractWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 수신한 텍스트 메시지 처리
        System.out.println("session");
        String payload = message.getPayload();
        // 원하는 작업 수행
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // WebSocket 연결이 확립된 후 호출되는 메서드
        // 연결이 성공적으로 이루어지면 여기에서 처리할 작업을 수행
        System.out.println("session");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // WebSocket 연결이 종료된 후 호출되는 메서드
        // 연결이 종료되면 여기에서 처리할 작업을 수행
    }
}
