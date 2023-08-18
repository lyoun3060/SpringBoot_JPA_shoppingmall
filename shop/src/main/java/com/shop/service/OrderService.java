package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entitiy.Item;
import com.shop.entitiy.Member;
import com.shop.entitiy.Order;
import com.shop.entitiy.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepositoy;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService{

    private final ItemRepository itemRepository;
    private final MemberRepositoy memberRepositoy;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()) //주문할 상품 조회
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepositoy.findByEmail(email); //현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보 조회

        List<OrderItem> orderItemsList = new ArrayList<>();
        //주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemsList.add(orderItem);

        //회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        Order order = Order.createOrder(member, orderItemsList);
        //생성한 주문 엔티티 저장
        orderRepository.save(order);

        return order.getId();
    }
}
