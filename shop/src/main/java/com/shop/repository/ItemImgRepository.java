package com.shop.repository;

import com.shop.entitiy.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> { //상품의 이미지 정보를 저장하기 위해 만든 인터페이스
}
