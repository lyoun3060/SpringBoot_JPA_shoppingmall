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

    @ManyToOne
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //<-부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeType All을 사용
    //하나의 주문이 여러개의 주문 상품을 갖을수 있도록 List자료형을 사용해서 매핑
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
