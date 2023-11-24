package com.shop.mapper;


import com.shop.domain.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    @Select("SELECT * FROM chatMessage WHERE roomNo=#{roomNo} AND status!='REMOVE' ORDER BY time ASC")
    public List<ChatMessage> chatMessageList(Long roomNo);
    // 해당 채팅방의 과거에 보낸 모든 채팅부터 정렬 (채팅 삭제를 대비한 remove)

    @Select("SELECT COUNT(*) FROM chatMessage WHERE roomNo=#{roomNo} AND status='UNREAD'")
    public int chatMessageUnread(Long roomNo);
    // 해당 채팅방의 안 읽은 모든 메시지 가져오기

    @Select("SELECT COUNT(*) FROM chatMessage WHERE receiver=#{receiver} AND status='UNREAD'")
    public int chatMessageUnreadAll(String receiver);
    // 나한테 온 모든 안 읽은 메시지 가져오기

    @Select("SELECT * FROM chatMessage ORDER BY chatNo DESC LIMIT 1")
    public ChatMessage chatMessageGetLast();
    // 마지막 채팅 가져오기

    @Insert("INSERT INTO chatMessage(type, roomNo, sender, receiver, message) VALUES(#{type}, #{roomNo}, #{sender}, #{receiver}, #{message})")
    public int chatMessageInsert(ChatMessage chatMessage);
    // 채팅 넣기

    @Update("UPDATE chatMessage SET status='READ' WHERE chatNo=#{chatNo} AND sender!=#{sender}")
    public int chatMessageReadUpdate(Long chatNo, String sender);
    // 상대방이 보낸 메시지만 읽음 처리

    @Update("UPDATE chatMessage SET status='READ' WHERE roomNo=#{roomNo} AND sender!=#{sender}")
    public int chatMessageReadUpdates(Long roomNo, String sender);
    // 상대방이 보낸 모든 메시지 읽음 처리

    @Update("UPDATE chatMessage SET status='REMOVE' WHERE chatNo=#{chatNo}")
    public int chatMessageRemoveUpdate(Long chatNo);
    // 채팅 삭제

    @Delete("DELETE FROM chatMessage WHERE chatNo=#{chatNo}")
    public int chatMessageDelete(Long chatNo);
}
