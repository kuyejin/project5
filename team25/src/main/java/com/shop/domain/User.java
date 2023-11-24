package com.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id; // 개인 번호 -> 전 userId

    @NotBlank(message = "아이디는 필수입니다.")
    private String userId; // 로그인 아이디 -> 전 loginId

    @NotBlank(message = "이름은 필수입니다.")
    private String userName; // 유저 이름

    //@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
     //       message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    private String confirmPassword;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "우편번호는 필수입니다.")
    private String postcode;

    @NotBlank(message = "기본 주소는 필수입니다.")
    private String addr1;

    private String addr2;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String tel;

    private String regdate;

    private int point;

    private String active;    // JOIN(활동 중) / DORMANT(휴면 중) / WITHDRAW(탈퇴)

    private Integer roleId;

    private String roleName;

}
