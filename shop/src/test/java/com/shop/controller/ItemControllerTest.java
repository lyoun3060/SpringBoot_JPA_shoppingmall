package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemFormTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")) //상품등록페이지에 get요청을 보냄
                .andDo(print()) //요청과 응답 메세지를 확인할 수 있도록 콘솔창에 출력
                .andExpect(status().isOk()); //응답 상태코드가 정상인지 확인하는것
    }


    @Test
    @DisplayName("상품 등록 페이지 일반회원 접근 테스트")
    @WithMockUser(username = "admin", roles = "USER")
    public void  itemFormNotAdminTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")) //상품등록페이지에 get요청을 보냄
                .andDo(print()) //요청과 응답 메세지를 확인할 수 있도록 콘솔창에 출력
                .andExpect(status().isForbidden()); //응답 상태코드가 비정상으로 나오는지 확인하는것
    }

}