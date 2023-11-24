package com.shop.config;

import com.shop.domain.UserFailLogin;
import com.shop.service.ProductService;
import com.shop.service.ProductServiceImpl;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserFailLogin loginFailHandler(){
        return new UserFailLogin();
    }

    //접근 보안 설정 관리자
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 권한에 따라 허용하는 url 설정
        // .antMatchers는 /login, /join 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용
        // 자원의 경로는 mvcMatchers 로
        http
                .authorizeRequests()
                .antMatchers("/", "/**", "/ex", "/upload/**").permitAll()
                .antMatchers("/member/login", "/member/join", "/member/joinPro", "/member/idCheck", "/member/emailCheck", "/chat/**", "/socket/**", "/news/news").permitAll()
                //.mvcMatchers("/","/templates/**","/ex/**","/resource/**","/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/teacher/**").hasAnyRole("ADMIN","TEACHER")
                .antMatchers("/member/update", "/member/out", "/member/updatePro").hasAnyRole("USER", "TEACHER", "ADMIN")
                .anyRequest().authenticated();

        // login 설정
        http
                .formLogin()
                .loginPage("/member/login")    // GET 요청 (login form을 보여줌)
                .loginProcessingUrl("/login")    // POST 요청 (login 창에 입력한 데이터를 처리)
                .failureHandler(loginFailHandler())
                .usernameParameter("userId")	// login에 필요한 id 값을 email로 설정 (default는 username)
                .passwordParameter("password")	// login에 필요한 password 값을 password(default)로 설정
                .defaultSuccessUrl("/");	// login에 성공하면 /로 redirect

        // logout 설정
        http
                .logout()
                .logoutUrl("/member/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");	// logout에 성공하면 /로 redirec

        //cors 방지 해제
        http.csrf().disable().cors().disable();

        //중복 로그인 방지
        http.sessionManagement()
                .sessionFixation().changeSessionId()
                .maximumSessions(1)
                .expiredSessionStrategy(new CustomSessionExpiredStrategy())
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry());

        return http.build();

    }

    private SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}