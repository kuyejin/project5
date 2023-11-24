package com.shop.service;


import com.shop.domain.Role;
import com.shop.domain.User;
import com.shop.domain.UserRole;
import com.shop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getUserList(){
        return userMapper.getUserList();
    }

    @Transactional
    public void userInsert(User user, Integer userId){
        if(!user.getUserId().equals("") && !user.getEmail().equals("")){
            user.setPassword(passwordEncoder.encode(user.getPassword())); // password 암호화 해서 저장
            userMapper.userInsert(user); // 회원가입 실시
            Role searchRole = userMapper.getRole(userId);
            User searchUser = userMapper.getLatestUser(); // 현재 가입한 회원의 ID
            UserRole userRole = new UserRole(searchUser.getId(), searchRole.getRoleId());
            userMapper.setUserRole(userRole); // UserRole 테이블로 저장 ~
        }
    }

    /*권한 바꾸기 */
    public void editUserRole(UserRole userRole){
        userMapper.editUserRole(userRole);
    }

    public User findById(Long id){
        return userMapper.findById(id);
    }

    public User findByEmail(String email){
        return userMapper.findByEmail(email);
    }

    public User findByUserId(String userId){
        return userMapper.findByUserId(userId);
    }

    public User getLatestUser(){
        return userMapper.getLatestUser();
    }

    public void edit(User user){
        userMapper.userEdit(user);
    }

    //회원 탈퇴
    public void withdraw(User user){
        userMapper.userDelete(user);
    }

    public void setUserRole(UserRole userRole){
        userMapper.setUserRole(userRole);
    }

    public UserRole getUserRole(Long id){
        return userMapper.getUserRole(id);
    }

    public PasswordEncoder passwordEncoder(){
        return this.passwordEncoder;
    }

    //아이디 중복 검사
    public int idDupCheck(User userId){
        int result = userMapper.idDupCheck(userId);
        return result;
    }

    // 회원 상세보기
    public User userDetail(Long id){
        return userMapper.userDetail(id);
    }

    // 권한 아이디 가져오기
    public int getRoleIdById(Long id) {
        return userMapper.getRoleIdById(id);
    }

    //  권한 가져 오기
    public Role getRole(Integer roleId){
        return userMapper.getRole(roleId);
    }

    // 계정 상태 가져오기
    public User getActive(Long id){
        return userMapper.getActive(id);
    }

    public User userDetailById(Long id){
        return userMapper.userDetailById(id);
    }
}
