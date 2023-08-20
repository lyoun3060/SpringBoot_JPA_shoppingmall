package com.shop.entitiy;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    //하나의 상품은 여러주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 다대일 단방향 매핑을 설정
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    //한 번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑을 먼저 설정
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;

    private int count;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        //주문할 상품과 주문 수량 세팅
        orderItem.setItem(item);
        orderItem.setCount(count);
        //현재시간 기준으로 상품가격을 주문 가격으로 세팅
        //상품 가격은 시간에 따라서 달라질 수 있음+쿠폰 할인 등(여기서는 고려하지 않음)
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){ //주문한 총 가격계산 메소드
        return orderPrice*count;
    }


    public void cancel(){
        this.getItem().addStock(count);
    }

/*BaseEntity를 상속받기 때문에 중복되서 필요없음
    private LocalDateTime regTime;

    private LocalDateTime updateTime;*/
}
