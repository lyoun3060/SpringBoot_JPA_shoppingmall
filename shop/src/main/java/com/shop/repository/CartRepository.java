package com.shop.repository;

import com.shop.entitiy.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //현재 로그인한 회원의 Cart엔티티를 찾기위해 추가
    Cart findByMemberId(Long memberId);

}
