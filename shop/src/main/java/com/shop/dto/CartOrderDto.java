package com.shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartOrderDto {

    private Long cartItemId;
    //장바구니 내에서 여러개의 상품을 주문하기때문에, CartOrderDto클래스가 자기자신을 List로 가지고 있게만듬
    private List<CartOrderDto> cartOrderDtoList;

}
