package com.shop.mapper;


import com.shop.domain.ChatRoom;
import com.shop.domain.ChatRoomVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    @Select("SELECT * FROM chatRoom")
    public List<ChatRoomVO> chatRoomList();

    @Select("SELECT * FROM chatRoom WHERE pno=#{pno} AND status!='BLOCK'")
    public List<ChatRoomVO> chatRoomProductList(Long pno);
    // 해당 상품의 차단하지 않은 모든 채팅방

    @Select("SELECT * FROM chatRoom where roomNo=#{roomNo}")
    public ChatRoomVO chatRoomGet(Long roomNo);
    // 채팅 메시지에서 바로 채팅방을 가져오기 위한 매퍼

    @Select("SELECT * FROM chatRoom WHERE pno=#{pno} AND buyer=#{buyer}")
    public ChatRoomVO chatRoomGetId(Long pno, String buyer);
    // ChatRoom 가져오기 (Insert에서 가져오기)

    @Select("SELECT COUNT(*) FROM chatRoom WHERE buyer=#{buyer} AND pno=#{pno}")
    public int chatRoomGetUnique(String buyer, Long pno);
    // buyer와 pno 컬럼이 같은 데이터를 가져와서 그 숫자를 셈

    //@Insert("INSERT INTO chatRoom(buyer, pno) SELECT userId, #{pno} FROM user WHERE userId = #{buyer}")
    @Insert("INSERT INTO chatRoom(buyer, pno) VALUES(#{buyer}, #{pno})")
    public void chatRoomInsert(String buyer, Long pno);
    // 새로 채팅방 만들기(기존 채팅방이 없으면)
    @Update("UPDATE chatRoom SET status='BLOCK' WHERE roomNo=#{roomNo}")
    public int chatRoomBlockUpdate(Long roomNo);
    // 채팅방 차단하기 (안 씀)

    @Delete("DELETE FROM chatroom WHERE roomNo=#{roomNo}")
    public int chatRoomDelete(Long roomNo);

    @Select("SELECT chatroom.*, product.seller FROM chatRoom JOIN product ON (chatroom.pno = product.pno) WHERE seller = #{id} OR buyer=#{id}")
    public List<ChatRoomVO> chatRoomMy(String id);
    // 내가 대화를 나눈 적 있는 모든 채팅방 가져오기
}
