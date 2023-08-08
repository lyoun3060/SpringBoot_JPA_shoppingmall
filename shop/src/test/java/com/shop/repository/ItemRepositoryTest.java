package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entitiy.Item;
import com.shop.entitiy.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품저장테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());

    }

    //테스트 코드 실행시 DB에 상품데이터가 없기때문에, 테스트 데이터 10개 정도 생성하기위한 메소드 ->d아래의 findByItemNmTest()에서 실행할것임
    public void createItemList(){
        for(int i=1;i<=10;i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }
    
    @Test
    @DisplayName("상품명 조회테스트,find()")
    //itemRepository 인터페이스에서 작성한 findByItemNm메소드를 호출하여, 테스트상품1을 전달
    //테스트상품1의 item객체들의 출력
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
            
        }
    }

    @Test
    @DisplayName("상품명 Or 상품상세설명")
    public void findItemOrItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan + 내림차순 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 Test")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 Test")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


    @PersistenceContext //EntityManager를 영속성 컨텍스트로 만들기위해 사용
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();

        //쿼리를 정적으로 만든다. -> 파일에 쿼리문을 미리 기입해둔뒤 필요시 가져와 사용하는것
        //쿼리를 동적으로 만든다. -> 특정 실행시점에 쿼리를 생성해서 사용하는것
        //여기서는 JPAQueryFactory가 쿼리를 동적으로 생성함
        // 생성자의 매개변수로 쿼리를 동적으로 컨트롤할수있게 엔티티매니저를 넣어준다
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        //querydsl을 통하여 쿼리를 생성하기 위하여, qitem 객체를 사용함
        QItem qItem = QItem.item;

        //자바 소스코드지만, SQL문과 비슷하게 소스를 작성할수 있게됨
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명"+"%"))
                .orderBy(qItem.price.desc());

        //JPAQuery 메소드 중 하나인 fetch를 이용하여 쿼리결과를 리스트를 반환함.
        //List<클래스> fetch()
        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    public void createItemList2(){
        for(int i=1;i<=5;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
        for(int i=6;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }

    }

    @Test
    @DisplayName("상품 Querydsl 조회테스트 2")
    public void queryDslTest2(){
        this.createItemList2();

        //BooleanBuilder 는 쿼리에 들어갈 조건을 만들어주는 빌더라고 생각하면됨
        //Predicate를 구현하고 있으며 메소드 체인형식으로 사용할 수 있음
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        //필요한 상품을 조회하는데 필요한 "and" 조건(.and를쓰고 뒤에 추가할 조건들)을 추가 하고 있음
        //if문을 통하여 판매상태가 Sell인 경우만 booleanBuilder에 판매상태 조건을 동적으로 추가하게 했음
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }


        //<데이터 페이징 조회>
        //데이터를 페이징해 조회하도록 pageRequest.of()메소드를 사용해 Pageable객체를 생성함
        //첫번째 인자로는 조회할 데이터 페이지의 번호,
        //두번째 인자로는 한 페이지당 조회할 데이터의 갯수를 넣어줍니다.
        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult =
                itemRepository.findAll(booleanBuilder, pageable); //QueryDslPredicateExecutor 인터페이스에서 정의한 findAll(()메소드를 사용해
                                                                  //조건에 맞는 데이터를 Page객체로 가져오게 하는것
        System.out.println("total elements : "+itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem : resultItemList){
            System.out.println(resultItem.toString());
        }

    }



}