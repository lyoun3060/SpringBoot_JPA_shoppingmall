package com.shop.repository;

import com.shop.entitiy.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Query 어노테이션을 이용한 주문이력 조회
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    // 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    //현재 로그인한 회원의 주문 갯수가 몇 개인지 파악
    Long countOrder(@Param("email") String email);

}
