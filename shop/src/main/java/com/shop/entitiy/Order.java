package com.shop.entitiy;

import com.shop.constant.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//정렬할 때 사용하는 order 키워드가 있기 때문에 Order엔티티에 매핑되는 테이블로 orders를 지정합니다.
@Table(name="orders")
@Data
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    //한명의 회원은 여러 번 주문을 할 수 있으믈 주문 엔티티 기준에서 다대일 단방향 매핑이 필요함
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    //주문 상품 엔티티와 일대다 매핑
    //외래키가 order_item테이블에 있으므로 연관관계의 주인은 OrderItem 엔티티
    //Order엔티티가 주인이 아니므로 "mappedBy"속성으로 연관 관계의 주인을 설정
    //mappedBy 속성은 양방향 연관 관계에서 한 쪽 엔티티의 필드가 다른 쪽 엔티티와 어떻게 연결되어 있는지를 지정하는 역할

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL, //<-부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeType All을 사용
            orphanRemoval = true, //이 옵션이 true로 설정되면, 부모 엔티티와 연관된 자식 엔티티가 더 이상 부모와 연관되지 않을 때 자동으로 삭제되는 것
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 갖을수 있도록 List자료형을 사용해서 매핑

    private LocalDateTime regTime;

    private LocalDateTime updateTime;


    //생성한 주문 상품 객체를 이용하여 주문 객체를 만드는 메소드
    public  void addOrderItem(OrderItem orderItem){

        //orderItems에는 주문 상품 정보들을 담아줄것(orderItem객체를 order객체에 orderItems에 추가)
        orderItems.add(orderItem);
        //Order엔티티와 OrderItem엔티티가 양방향 참조 관계이므로, orderItem객체에도 order객체를 세팅해야함
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        //상품을 주문한 회원의 정보를 세팅
        order.setMember(member);
        //상품 페이지에서는 1개의 상품을 주문하지만, 장바구니에서는 한번에 여러개의 상품을 주문할 수 있다.
        //따라서 여러 개의 주문 상품을 담을 수 잇도록 리스트 형태로 파라미터값을 받아, 주문 객체에 orderItem객체를 추가
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); //<- 주문상태를 ORDER로 세팅
        order.setOrderDate(LocalDateTime.now()); //<- 현재 시간을 주문시간으로 세팅
        return order;
    }

    public  int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
