package com.shop.controller;

import com.shop.domain.*;
import com.shop.service.ChatService;
import com.shop.domain.*;
import com.shop.service.PayService;
import com.shop.service.ProductService;
import com.shop.service.ReviewService;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChatService chatService;


    @Autowired
    private PayService payService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String home(Model model){ // 인증된 사용자 정보 보여줌
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //토큰에 저장된 사용자의 고유번호 id값
        User user = userService.findById(id);
        user.setPassword(null);
        model.addAttribute("user",user);
        return "index";
    }

    //User 테이블의 전체 정보 보여줌
    @GetMapping("/userList")
    public String getUserList(Model model){
        List<User> userList = userService.getUserList();
        model.addAttribute("list", userList);
        return "member/userList";
    }

    //로그인 폼 출력
    @GetMapping("/login")
    public String loginForm(){
        return"member/login";
    }


    @GetMapping("/join")
    public String joinForm(Model model, User user){
        model.addAttribute("user", user);
        return "member/joinForm";
    }

    @PostMapping("/join")
    public String userInsert(@Valid User user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "member/joinForm";
        }
        userService.userInsert(user, 5);
        model.addAttribute("user", user);
        return "redirect:/";
    }

    //회원 정보 수정폼
    @GetMapping("/update")
    public String updateForm(Model model){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "member/updateForm";
    }

    //회원 정보 수정
    @PostMapping("/update")
    public String edit(User user){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setId(id);
        userService.edit(user);
        return "redirect:/";
    }

    //아이디 중복 검사
    @ResponseBody // ajax 값 변환 위해 필요
    @GetMapping("/idDupCheck")
    public int idDupCheck(User userId){
        int result = userService.idDupCheck(userId); // 중복 확인 값 int로 받음
        return result;
    }

    /*
    @GetMapping("/editRole")
    public String updateRoleForm(Model model){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(id);
        Role role = userService.getRol
        UserRole userRole = userService.getUserRole(id);
        model.addAttribute("user", user);
        model.addAttribute("userRole", userRole);
        return "member/updateUserForm";
    }


    @PostMapping("/editRole")
    public String editUserRole(Model model, UserRole userRole){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Integer roleId = userRole.setId(id);
        //userRole.setRoleId(roleId);
        return null;
    }

     */



    //회원 탈퇴



   /* @GetMapping("/mypage")
    public String getMyInfo(Model model){
        //로그인 후 사용자 정보 가져와서 모델에 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        model.addAttribute("username",username);
        return "test";
    }*/

    @GetMapping("/loginInfo")
    public String memberInfo(Principal principal, ModelMap modelMap){
        String loginId = principal.getName();
        User user = userService.findByUserId(loginId);
        modelMap.addAttribute("user", user);
        return "member/myinfo";
    }



    //내상점
    //@RequestMapping("/myProList")
    @RequestMapping(value = "/myProList", method = RequestMethod.GET)
    public String myProductList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId  = authentication.getName();


        List<Product> myproList = productService.findByUserId(userId);
        System.out.println(myproList);
        model.addAttribute("myproList", myproList);
        //소윤의 구매내역
        List<Pay> myPayList = payService.myPayListByUserId(userId);


        //리뷰 체크
        Review review = new Review();
        for(Pay re: myPayList){
            review.setPno(re.getPno());
            review.setId(userId);
            int check = reviewService.reviewCheck(review);

            System.out.println("check: " + check );
            re.setCheck(check);

        }

        model.addAttribute("myPayList",myPayList);



        // 내가 쓴 후기
        List<Review> proReview= reviewService.proReview(userId);
        System.out.println(proReview);
        model.addAttribute("proReview", proReview);

        //내가 받은 후기
        List<Review> proSellerReview= reviewService.proSellerReview(userId);
        System.out.println(proSellerReview);
        model.addAttribute("proSellerReview", proSellerReview);


        //좋아요
        //pno, userId
        List<Likes> proLikes = productService.getByIdLikeList(userId);
        List<Product> proList = new ArrayList<>();
        for (Likes pro: proLikes) {
            System.out.println(pro);
            proList.add(productService.getProduct(pro.getPno()));
        }

        System.out.println(proList);
        model.addAttribute("proLikes", proLikes);
        model.addAttribute("proList", proList);



        return "member/myProductList";
    }

    //나의 채팅방 목록
    @GetMapping("myChatList")
    public String myChat(HttpServletRequest request, Model model, Principal principal){
        if(principal != null) {
            String userId  = principal.getName();
            model.addAttribute("userId", userId);

            List<ChatRoomVO> rooms = chatService.chatRoomMy(userId);
            for(ChatRoomVO chatRoom : rooms){
                User buyer = userService.findByUserId(chatRoom.getBuyer());
                chatRoom.setBuyerNm(buyer.getUserName());
            }
            model.addAttribute("rooms", rooms);
        }
        return "member/myChatList";
    }

    // 채팅하기
    @GetMapping("myChat")
    public String myChat(HttpServletRequest request, ModelMap modelMap, Principal principal, Model model) {
        //사용자의 아이디 가져오기
        String userId = principal.getName();
        model.addAttribute("userId", userId);

        //또는 SecurityContextHolder.getContext().getAuthentication().getName();
        String buyer = request.getParameter("buyer");//구매희망자
        Long pno = Long.valueOf(request.getParameter("pno")); // 상품 고유번호

        // 채팅방이 없으면 새로 추가, 있으면 가져오기
        ChatRoomVO room = chatService.chatRoomInsert(buyer, pno);
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

        return "member/myChat";
    }







}