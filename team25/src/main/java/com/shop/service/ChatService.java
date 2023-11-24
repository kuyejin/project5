package com.shop.service;


import com.shop.domain.ChatMessage;
import com.shop.domain.ChatRoom;
import com.shop.domain.ChatRoomVO;

import java.util.List;

public interface ChatService {
    public List<ChatRoomVO> chatRoomProductList(Long pno);
    public ChatRoomVO chatRoomGetNo(Long roomNo);
    public ChatRoomVO chatRoomInsert(String buyer, Long pno);
    public int chatRoomBlockUpdate(Long roomNo);

    public List<ChatMessage> chatMessageList(Long roomNo);
    public ChatMessage chatMessageInsert(ChatMessage chatMessage);
    public int chatMessageReadUpdate(Long chatNo, String sender);
    public int chatMessageReadUpdates(Long roomNo, String sender);
    public int chatMessageRemoveUpdate(Long chatNo);

    public int chatMessageUnreadAll(String receiver);
    public List<ChatRoomVO> chatRoomMy(String id);
}
