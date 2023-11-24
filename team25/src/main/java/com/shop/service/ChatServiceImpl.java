package com.shop.service;

import com.shop.domain.ChatMessage;
import com.shop.domain.ChatRoom;
import com.shop.domain.ChatRoomVO;
import com.shop.mapper.ChatMessageMapper;
import com.shop.mapper.ChatRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatRoomMapper roomMapper;

    @Autowired
    ChatMessageMapper chatMapper;

    @Override
    public List<ChatRoomVO> chatRoomProductList(Long pno) {
        return roomMapper.chatRoomProductList(pno);
    }
    // 해당 상품의 모든 채팅방 가져오기
    // 즉 판매자가 구매 희망자들의 채팅 리스트를 볼 수 있음

    @Override
    public ChatRoomVO chatRoomGetNo(Long roomNo) {
        return roomMapper.chatRoomGet(roomNo);
    }
    // 해당 채팅방 가져오기

    @Override
    public ChatRoomVO chatRoomInsert(String buyer, Long pno) {
        if(roomMapper.chatRoomGetUnique(buyer, pno)<1){
            roomMapper.chatRoomInsert(buyer, pno);
            // buyer 와 pno 가 같은 데이터가 없다면, 해당 상품과 구매자에 대한 채팅방이 없다는 뜻! 새로 채팅방을 만든다.
            // 새로운 구매 희망자가 나타날 때 새로 채팅방을 만든다.
        }

        return roomMapper.chatRoomGetId(pno, buyer); // 채팅방 가져오기
    }

    @Override
    public int chatRoomBlockUpdate(Long roomNo) {
        return roomMapper.chatRoomBlockUpdate(roomNo);
    }
    // 채팅 차단하기

    @Override
    public List<ChatMessage> chatMessageList(Long roomNo) {
        return chatMapper.chatMessageList(roomNo);
    }
    // 해당 채팅방의 모든 채팅 가져오기

    @Override
    public ChatMessage chatMessageInsert(ChatMessage chatMessage) {
        Long roomNo = chatMessage.getRoomNo();
        ChatRoomVO room = roomMapper.chatRoomGet(roomNo);
        if(room.getStatus().equals("BLOCK")){
            return null; // 차단된 경우에는 메시지 전송하지 않음.
        }
        chatMapper.chatMessageInsert(chatMessage); // 채팅 넣기
        return chatMapper.chatMessageGetLast(); // 내가 방금 전에 넣은 채팅 가져오기
    }

    @Override
    public int chatMessageReadUpdate(Long chatNo, String sender) {
        return chatMapper.chatMessageReadUpdate(chatNo, sender);
    }
    // 한 채팅에 대한 읽음 처리

    @Override
    public int chatMessageReadUpdates(Long roomNo, String sender) {
        return chatMapper.chatMessageReadUpdates(roomNo, sender);
    }
    // 모든 채팅에 대한 읽음 처리

    @Override
    public int chatMessageRemoveUpdate(Long chatNo) {
        return chatMapper.chatMessageRemoveUpdate(chatNo);
    }
    // 혹시 채팅을 삭제할 수도 있을까

    @Override
    public int chatMessageUnreadAll(String receiver) {
        return chatMapper.chatMessageUnreadAll(receiver);
    }
    // 나한테 온 모든 읽지 않은 메시지 수

    @Override
    public List<ChatRoomVO> chatRoomMy(String id) {
        return roomMapper.chatRoomMy(id);
    }
    // 내가 채팅을 했던 모든 채팅방
}
