package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection
    // 프로젝션(Projection)은 데이터베이스에서 필요한 데이터를 선택적으로 가져오거나 변환하여 반환하는 작업
    // @QueryProjection을 활용하여 item객체를 별도의 과정없이 바로 DTO객체로 변환 <-사용할때 maven compile실행필요
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
