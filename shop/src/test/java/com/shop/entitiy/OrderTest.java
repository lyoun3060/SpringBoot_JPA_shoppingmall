package com.shop.entitiy;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepositoy;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Table;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); //아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order엔티티에 담음

        }

        orderRepository.saveAndFlush(order); //order엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영
        em.clear();//영속성 컨텍스트 초기화

        //영속성 컨텍스트를 초기화 했기 때문에 데이터베이스에서 주문 엔티티를 조회.
        //select 쿼리문이 실행되는것을 콘솔창에서 확인가능
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());

    }




    /*고아객체 테스트부분*/

    @Autowired
    MemberRepositoy memberRepositoy;

    public Order createOrder() {//주문데이터를 생성해서 저장하는 메소드생성

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); //아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order엔티티에 담음
        }

        Member member = new Member();
        memberRepositoy.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;

    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0); //order엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 삭제합니다.
        em.flush();//>부모 엔티티와 연관관계가 끊어졌기 때문에 결과는 고아객체를 삭제하는 쿼리문이 실행됨
    }
    //※Cascade REMOVE와의 차이점 :
    //Cascade REMOVE의 옵션은 부모 엔티티가 삭제될때 연관된 자식 엔티티도 같이 삭제



    /*------지연로딩 테스트부분---------*/
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order =this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        //영속성 컨텍스트의 상태를 초기화 한 후 order엔티티에 저장했던 주문 상품 아이디를 이용하여 orderItem을 데이터베이스에서 다시 조회
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        //orderItem에 있는 order객체의 클래스를 출력
        System.out.println("Order class : "+orderItem.getOrder().getClass());
        System.out.println("=========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=========================");
    }



}