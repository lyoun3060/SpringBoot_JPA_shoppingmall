package com.shop.repository;

import com.shop.entitiy.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
                                        //Predicate는 JPA 엔티티의 필드들을 조합하여 동적인 쿼리 조건을 표현하는 객체
                                        //QuerydslPredicateExecutor는 메소드 이름 규칙을 기반으로 쿼리 조건을 자동으로 생성합니다.
                                        //예를 들어, 메소드 이름에 "findBy"를 포함하고 필드명을 지정하면, 해당 필드를 기준으로 동적인 쿼리를 생성.
                                        //+페이징, 정렬기능 지원

    //find(엔티티이름)By(변수이름) = >find(엔티티명 생략가능)By(ItemNm)
    //검색하기 위한 것
    List<Item> findByItemNm(String itemNm);

    //Or조건을 사용한 find()
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);


    //LessThan조건을 사용한 find()
    List<Item> findByPriceLessThan(Integer price);


    //LessThan조건 + OrderBy정렬을 사용한 find()
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //JPQL의 @Query를 사용한 복잡한 조건의 쿼리메소드 처리하기
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    //JPQL의 @Query-nativeQuery속성 사용
    @Query(value = "select * from item i where i.item_Detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);


}
