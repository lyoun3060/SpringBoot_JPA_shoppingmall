package com.shop.exception;


//고객이 주문을 할때 재고보다 많이 주문하는 경우를 위한 클래
public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message){
        super(message);
    }
}
