package com.shop.entitiy;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;  //장바구니에 담을 상품의 정보를 알아야 하

    private int count; // 같은 상품을 장바구니에 몇개담을 지 저장
}
