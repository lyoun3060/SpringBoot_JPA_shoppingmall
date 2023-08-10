package com.shop.entitiy;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    //하나의 상품은 여러주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 다대일 단방향 매핑을 설정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    //한 번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑을 먼저 설정
    private Item item;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
