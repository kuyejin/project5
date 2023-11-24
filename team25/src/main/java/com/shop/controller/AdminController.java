package com.shop.controller;

import com.shop.domain.Role;
import com.shop.domain.User;
import com.shop.domain.UserRole;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model){
        List<User> userList = userService.getUserList();
        model.addAttribute("userList", userList);
        return "admin/index";
    }


    // 회원 상세보기 - 관리자 버전 -
    @GetMapping("/userDetail/{id}")
    public String userDetail1(@PathVariable("id") Long id, Model model){
        User user = userService.userDetail(id);
        model.addAttribute("user", user);
        return "admin/userDetail";
    }

    // 관리자 회원 권한 변경폼 가져오기
    @GetMapping("/editRole/{id}")
    public String updateRoleForm(@PathVariable("id") Long id,Model model){
        UserRole userRole = userService.getUserRole(id);
        model.addAttribute("userRole", userRole);
        return "member/updateUserRole";
    }

    // 관리자 회원 권한 변경
    @PostMapping("/editRole/{id}")
    public String editUserRole(@PathVariable("id") Long id ,Model model, UserRole userRole, User user){
        Integer roleId  = userService.getRoleIdById(id);
        userRole.setId(id);
        userRole.setRoleId(roleId);
        userService.editUserRole(userRole);
        model.addAttribute("userRole", userRole);
        return "admin/userList";
    }

    //User 테이블의 전체 정보 보여줌
    /*@GetMapping("/userList")
    public String getUserList(Model model){
        List<User> userList = userService.getUserList();
        model.addAttribute("userList", userList);
        return "admin/userList";
    }

     */


    //회원 정보 수정폼
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id,Model model){
        User user = userService.findById(id);
        UserRole userrole = userService.getUserRole(id);
        model.addAttribute("user", user);
        model.addAttribute("userRole", userrole);
        return "admin/updateForm";
    }

    //회원 정보 수정
    @PostMapping("/update/{id}")
    public String edit(@PathVariable("id") Long id,User user,UserRole userRole, Model model){
        user.setId(id);
        userRole.setId(id);
        userService.edit(user);
        userService.editUserRole(userRole);
        model.addAttribute("user",user);
        model.addAttribute("userRole", userRole);
        return "redirect:/admin/userList";
    }

    //회원 탈퇴
    @PostMapping("/editActive")
    public String withdraw(User user, HttpSession session, Model model){
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setActive(user.getActive());
        user.setId(id);
        userService.withdraw(user);
        //토큰은 삭제 하지 않고 기록만 남길 수 있게??
        return "redirect:/";
    }





}
