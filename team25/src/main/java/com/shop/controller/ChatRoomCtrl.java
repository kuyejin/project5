package com.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.shop.domain.*;
import com.shop.service.ChatService;
import com.shop.service.ProductService;
import com.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@Controller
@Slf4j
@RequestMapping("/chat/")
public class ChatRoomCtrl {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ChatService chatService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    // 채팅방 입장
    @GetMapping("roomEnter")
    public String roomEnter(HttpServletRequest request, Model model, Principal principal){

        //사용자의 아이디 가져오기
        String userId = principal.getName();
        model.addAttribute("userId", userId);

        //또는 SecurityContextHolder.getContext().getAuthentication().getName();
        String buyer = request.getParameter("buyer");//구매희망자
        Long pno = Long.valueOf(request.getParameter("pno")); // 상품 고유번호

        // 채팅방이 없으면 새로 추가, 있으면 가져오기
        ChatRoomVO room = chatService.chatRoomInsert(userId, pno);
        model.addAttribute("room", room);


        // 기존의 채팅 내역 가져오기
        Long roomNo = room.getRoomNo();
        List<ChatMessage> chats = chatService.chatMessageList(roomNo);
        model.addAttribute("chats", chats);

        // 채팅방에 들어가면 기존에 안 읽은 메시지 읽음 처리
        //chatService.chatMessageReadUpdates(roomNo, userId);

        // 채팅방 상대 이름 띄우기
        // 채팅방은 구매자 기준으로 저장되므로, 구매자인 경우 product 에서 seller 가져오기
        Product product = productService.getProduct(pno);

        if (userId.equals(room.getBuyer())) {
            // 로그인한 사람이 구매자인 경우 판매자의 이름
            model.addAttribute("roomName", product.getSeller());
        } else {
            // 로그인한 사람이 판매자인 경우 구매자의 이름
            model.addAttribute("roomName", room.getBuyer());
        }

        return "chat/chat";
    }



    @GetMapping("roomList")
    public String roomList(HttpServletRequest request, Model model){
        Long pno = Long.valueOf(request.getParameter("pno"));
        model.addAttribute("pno", pno);

        List<ChatRoomVO> chatRooms = chatService.chatRoomProductList(pno);
        model.addAttribute("rooms", chatRooms);

        return "chat/chatList";
    }
/*
    @PostMapping("blockRoom")
    @ResponseBody
    public String blockRoom(HttpServletRequest request){
        Long roomNo = Long.valueOf(request.getParameter("pno"));
        int returnNo = chatService.chatRoomBlockUpdate(roomNo);
        if(returnNo>0){
            return "Block Successfully";
        }

        return "Something went wrong";
    }


    @PostMapping("readRoom")
    @ResponseBody
    public String readRoom(HttpServletRequest request){
        Long roomNo = Long.valueOf(request.getParameter("pno"));
        String sender = request.getParameter("buyer");

        int returnNo = chatService.chatMessageReadUpdates(roomNo, sender);
        if(returnNo>0){
            return "Success";
        }

        return "Something went wrong";
    }
*/

    @PostMapping("insertChat")
    @ResponseBody
    public ChatMessage insertChat(@RequestParam String message) throws JsonProcessingException {
        ChatMessage chat = mapper.readValue(message, ChatMessage.class);

        System.out.println(">>>>>>>>>>>>>>>>>>>" + chat);

        return chatService.chatMessageInsert(chat);
    }
    // 채팅 넣기


    @PostMapping("readChat")
    @ResponseBody
    public String readChat(@RequestParam String message, @RequestParam String user) throws JsonProcessingException {
        ChatMessage chat = mapper.readValue(message, ChatMessage.class);

        chatService.chatMessageReadUpdate(chat.getChatNo(), user);

        return "readChat Completed";
    }



    @GetMapping("unreadAll")
    @ResponseBody
    public int unreadAll(@RequestParam String receiver){
        return chatService.chatMessageUnreadAll(receiver);
    }
    // 안 읽은 모든 채팅 개수 가져오기
}