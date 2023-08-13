package com.shop.entitiy;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Data
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    //상품 엔티티와 다대일 단방향 관계로 매핑
    //또한 지연로딩을 사용하여 매핑된 상품 엔티티 정보가 필요할 경우 데이터를 조회하도록 함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    //원본 이미지 파일명, 업데이트할 이미지파일명, 이미지 경로를 파라미터로 입력받아서 이미지 정보를 업데이트하는 메소드
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
