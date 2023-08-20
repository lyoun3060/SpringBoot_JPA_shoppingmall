package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.CartItemDto;
import com.shop.entitiy.CartItem;
import com.shop.entitiy.Item;
import com.shop.entitiy.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepositoy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepositoy memberRepositoy;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@email.com");
        return memberRepositoy.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();


        //장바구니에 담을 상품과 수량을 cartItemDto객체에 세팅
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        //상품을 장바구니에 담는 로직 호출, 생성된 장바구니에 상품 아이디를 cartItemId변수에 저장
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }

}