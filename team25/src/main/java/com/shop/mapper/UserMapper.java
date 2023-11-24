package com.shop.mapper;

import com.shop.domain.Role;
import com.shop.domain.User;
import com.shop.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {

    List<User> getUserList(); // User 테이블 가져오기

    //회원 가입
    void userInsert(User user);

    //회원 고유 번호 찾기
    User findById(Long id);

    //회원 아이디 찾기
    User findByUserId(String userId);

    //회원 이메일 찾기
    User findByEmail(String email);

    //최근 가입 회원 찾기
    User getLatestUser();

    //회원 정보 수정
    void userEdit(User user);

    //회원 탈퇴
    void userDelete(User user);

    //회원 권한 설정
    void setUserRole(UserRole userRole);

    //회원 권한 가져오기
    UserRole getUserRole(Long id);

    // 권한 내용 가져오기
    Role getRole(Integer roleId);

    //아이디 중복 검사
    int idDupCheck(User userId);

    //권한 변경
    void editUserRole(UserRole userRole);

    //회원 상세보기
    User userDetail(Long id);

    // 권한 변경
    void editUserRole(Long id);

    // id로 권한 내용 가져오기
    int getRoleIdById(Long id);

    User getActive(Long id);


   User userDetailById(Long id);
}
