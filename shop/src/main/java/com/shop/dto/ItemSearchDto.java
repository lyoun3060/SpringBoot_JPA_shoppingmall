package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    //현재 시간과 상품 등록일을 비교해서 상품데이터를 조회
    private String searchDateType;

    //상품 판매상태를 기준으로 상품 데이터를 조회
    private ItemSellStatus searchSellStatus;

    //상품의 조회할때 어떤 유형으로 조회할 지 선택
    // ㄴ ItemNm: 상품명
    // ㄴ createdBy : 상품 등록자 아이디
    private String searchBy;

    //조회할 검색어를 저장할 변수.
    //searchBy가 itemNm인 경우 상품 등록자 아이디 기준으로 검것
    //createdBy일 경우 상품 등록자 아이디 기준으로 검색
    private String searchQuery = "";

}
