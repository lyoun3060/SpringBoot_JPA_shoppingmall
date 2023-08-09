package com.shop.entitiy;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepositoy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepositoy memberRepositoy;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("김수장");
        memberFormDto.setAddress("서울시 마포구 김수동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepositoy.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);


        //JPA는 영속성 컨텍스트에 데이터를 저장후 트랜잭션이 끝날때 flush()를 호출하여 DB에 반영
        //member, 장바구니 엔티티를 영속성 컨텍스트에 저장후 엔티티 매니저로부터 강제로 flush()를 호출하여 db에 반영하는것
        em.flush();
        em.clear();

        //저장된 장바구니 엔티티를 조회
        Cart saveCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        //처음에 저장한 member엔티티의 id와, savedCart에 매핑된 member엔티티의 id를 비교하는것
        assertEquals(saveCart.getMember().getId(), member.getId());
    }

}