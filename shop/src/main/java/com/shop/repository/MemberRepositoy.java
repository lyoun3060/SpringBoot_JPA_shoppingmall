package com.shop.repository;

import com.shop.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepositoy extends JpaRepository<Member, Long> {

    //회원가입시 중복회원이 있는지 검사하기 위해 이메일로 회원을 검사할수 있는 쿼리 메소드를 작성함
    Member findByEmail(String email);

}
