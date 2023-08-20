package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entitiy.Order;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderHistDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    public OrderHistDto(Order order){ //order객체를 파라미터로 받아서 멤버변수값을 세팅, 주문날짜는 포맷을 수정해서(yyyy)형태로
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    //orderItemDto객체를 주문 상품 리스트에 추가하는 메서드입니다.
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
