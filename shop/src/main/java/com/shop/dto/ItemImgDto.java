package com.shop.dto;

import com.shop.entitiy.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImgDto { //상품 저장 후 상품이미지에 대한 데이터를 전달할 DTO클래스

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    //멤버 변수로 ModelMapper객체를 추가
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){
        //ItemImg엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto로 값을 복사해서 반환
        //static 메소드로 선언해 ItemImgDto객체를 생성하지 않아도 호출할 수 있도록 함
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
