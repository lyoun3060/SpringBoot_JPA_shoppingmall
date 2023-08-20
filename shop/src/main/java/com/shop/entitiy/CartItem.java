package com.shop.entitiy;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="cart_item")
public class CartItem extends BaseEntity{

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



    //장바구니에 담을 상품엔티티를 생성하는 메소드와, 장바구니에 담을 수량을 증가시켜주는 메소드
    public static CartItem createCartItem(Cart cart, Item item, int count){

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    //장바구니에 기존에 담겨있는 상품에, 해당하는 상품을 추가로 장바구니에 담을때 사용하는 메서드
    //기존 수량+추가로 담을 수량
    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
