package com.shop.entitiy;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Data
public class Cart {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    //멤버 엔티티와 일대일 관계라는것을 선포
    @OneToOne
    //joincolumn을 사용하여 멤버엔티티에 member_id를 외래키로 지정
    //만약 지정해주지 않으면 JPA가 알아서 찾아 지정하지만, 원하는대로 되지 않는경우가 있기때문에 직접 지정하는게 나음
    @JoinColumn(name="member_id")
    private Member member;

}
