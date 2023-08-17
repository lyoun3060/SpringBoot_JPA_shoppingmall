package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entitiy.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepositoryCustom {
    //상품 조회 조건을 담고 있는 itemSearchDto 객체와
    //페이징 정보를 담고있는 pageable객체를 파라미터로 받는 getAdminItemPage 메소드를 정의한것
    //반환 데이터로 Page<Item>객체를 반환
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
