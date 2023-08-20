package com.shop.repository;

import com.shop.entitiy.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> { //상품의 이미지 정보를 저장하기 위해 만든 인터페이스

    //이미지가 잘 저장됬는지를 테스트하기 위한 메소드
    //매개변수로 넘겨준 상품 아이디를 가지며, 상품 이미지 아이디의 오름차순으로 가져오는 쿼리메소드를 만들것
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);


    //구매 이력페이지에서 주문상품의 대표 이미지를 보여주기위해 추가
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}
