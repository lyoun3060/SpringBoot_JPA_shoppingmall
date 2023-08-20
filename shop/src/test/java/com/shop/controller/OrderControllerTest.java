package com.shop.controller;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.OrderDto;
import com.shop.entitiy.Item;
import com.shop.entitiy.Member;
import com.shop.entitiy.Order;
import com.shop.entitiy.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepositoy;
import com.shop.repository.OrderRepository;
import com.shop.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepositoy memberRepositoy;

    public Item saveItem(){ //테스트를 위한 아이템
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){ //테스트를 위한 멤버
        Member member = new Member();
        member.setEmail("test@email.com");
        return memberRepositoy.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public  void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10); //주문할 상품수량을 orderDto객체에 세팅
        orderDto.setItemId(item.getId()); //주문할 상품을 orderDto객체에 세팅

        //주문 로직 호출결과 생성된 주문 번호를 orderId 변수에 저장
        Long orderId = orderService.order(orderDto, member.getEmail());

        //주문 번호를 이용하여 저정된 주문 정보를 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityExistsException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        //주문한 상품의 총 가격
        int totalPrice = orderDto.getCount()*item.getPrice();

        //주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 가격을 비교하는 테스트(같으면 성공적으로 실행됨)
        assertEquals(totalPrice, order.getTotalPrice());
    }


}