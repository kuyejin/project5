package com.shop.config;

import com.shop.domain.UserRole;
import com.shop.service.UserService;
import com.shop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        String userId = (String) authentication.getPrincipal(); // 로그인 창에 입력한 아이디
        String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //userService.passwordEncoder();
        UsernamePasswordAuthenticationToken token;
        User user = userService.findByUserId(userId);
        UserRole userRole = userService.getUserRole(user.getId());

        if (user != null && passwordEncoder.matches(password, user.getPassword())) { // 일치하는 user 정보가 있는지 확인
            System.out.println("비밀번호 맞음");
            List<GrantedAuthority> roles = new ArrayList<>();
            if(userRole.getRoleId()==1){
                roles.add(new SimpleGrantedAuthority("ADMIN")); // 권한 부여
            } else if(userRole.getRoleId()==2){
                roles.add(new SimpleGrantedAuthority("TEACHER")); // 권한 부여
            } else if(userRole.getRoleId()==3){
                roles.add(new SimpleGrantedAuthority("STAFF")); // 권한 부여
            } else if(userRole.getRoleId()==4){
                roles.add(new SimpleGrantedAuthority("MANAGER")); // 권한 부여
            }else if(userRole.getRoleId()==5){
                roles.add(new SimpleGrantedAuthority("USER")); // 회원 권한
            }
            // userId - 로그인 시 사용할 아이디를 principal로 사용할 때
            token = new UsernamePasswordAuthenticationToken(user.getUserId(), null, roles);

            return token;
        }

        throw new BadCredentialsException("No such user or wrong password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}