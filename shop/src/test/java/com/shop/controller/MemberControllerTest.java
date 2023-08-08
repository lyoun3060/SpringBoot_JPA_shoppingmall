package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entitiy.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
//MockMVC는 Spring Framework에서 제공하는 테스트용 클래스
//웹 애플리케이션의 컨트롤러를 테스트할 때 사용함.
// 서버를 실제로 실행하지 않고도 컨트롤러의 동작을 시뮬레이션하고 테스트할 수 있음
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    //실제객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜객체
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    //테스트에 사용할 회원등록하는 메소드
    public Member createMember(String email, String password){ 
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("부산시 해안대구 좌동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }


    
    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                    .loginProcessingUrl("/members/login") //회원가입 메소드를 실행한 후 가입된 회원 정보로 로그인이 되는지 테스트하는것
                    .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //로그인이 성공하여 인증되었다면 테스트 코드가 통과
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login") //회원가입 메소드를 실행한 후 가입된 회원 정보로 로그인이 되는지 테스트하는것
                        .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); //로그인이 실패되었다면 테스트 코드가 통과
    }


}