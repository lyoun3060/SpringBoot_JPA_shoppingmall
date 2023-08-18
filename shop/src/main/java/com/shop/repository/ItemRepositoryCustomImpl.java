package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entitiy.Item;
import com.shop.entitiy.QItem;
import com.shop.entitiy.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;// 동적으로 쿼리를 생성하기 위해 JPAQueryFactory클래스 사용

    //JPAQueryFactory의 생성지로 EntitiyManager객체를 넣기
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        //상품 판매조건이 전체null인경우 null리턴 -> null로 결과값이 나오면 where절에서 해당 조건은 무시됨
        //다시말해 상품판매조건이 판매중 or 품절상태인 조건의 상품들만 조회됨
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        //searchDateType의 값에 따라서 dateTime의 갑을 이전 시간의 값으로 세팅 후, 해당 시간이 후로 등록된 상품만 조회
        //예를들어 searchDateType의 값이 1m인 경우 dateTime의 시간을 한 달 전으로 세팅 후 최근 한 달 동안 등록된 상품만 조회하도록 함
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType==null){
            return null;
        }else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }

        return null;
    }

    @Override
    //Item엔티티를 조회하고 페이징 처리하여 Page<Item>객체를 생성하는 메서드
    //ItemSearchDto itemSearchDto: 검색 조건을 담은 DTO 객체로, 검색 조건을 받아와서 쿼리를 구성합니다.
    //Pageable pageable: 페이징 정보를 담은 객체로, 페이지 번호, 페이지 크기, 정렬 정보 등을 포함합니다.
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        List<Item> content = queryFactory //queryFactory를 사용하여 데이터베이스에서 Item 엔티티를 조회하는 쿼리를 구성
                .selectFrom(QItem.item)
                // where 절을 통해 검색 조건을 추가합니다. regDtsAfter(), searchSellStatusEq(), searchByLike() 등의 메서드를 사용하여 조건을 설정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc()) //내림차순 정렬
                // offset과 limit을 사용하여 페이징 처리된 데이터를 가져
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //(방법1)
        //조회한 데이터를 Page클래스의 구현체인 PageImpl객체로 반환
        // content.size()는 현재 페이지에 있는 데이터의 개수를 나타냄
        /*return new PageImpl<>(content, pageable, content.size());*/

        //(방법2)
        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression itemNmLike(String seachQuery){ //검색어가 null이 아니면 해당검색어가 포함되는 상품을 조회하는 조건반환
        return StringUtils.isEmpty(seachQuery) ? null : QItem.item.itemNm.like("%" + seachQuery + "%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(// QMainItemDto의 생성자에 반환할 값들을 넣음 / @QueryProjection을 사용하여 바로 DTO로 조회가능(변환과정생략)
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                        )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg와 item을 내부조인
                .where(itemImg.repimgYn.eq("Y")) //상품 이미지의 경우 대표 이미지만 불러오도록 하는것
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        /*
        * 방법1과 방법2의 차이
        * (2)의 코드는 QueryDSL을 사용하여 DB에서 실제로 전체 아이템 수(total)를 조회하는 방식입니다.
        *  Wildcard.count를 사용하여 SELECT 쿼리를 생성하고, fetchOne()을 호출하여 전체 아이템 수를 가져옵니다.
        *  이 방식은 실제 데이터베이스에서 전체 아이템 수를 가져오기 때문에 정확한 값을 반환합니다.
        * (1)의 코드는 (2)의 방식과 달리 이미 페이지네이션된 콘텐츠(List<MainItemDto> content)의 크기를 사용하여 전체 아이템 수(total)를 구합니다.
        *  이 방식은 이미 쿼리를 실행하여 가져온 결과를 사용하므로 별도의 쿼리 실행 없이 처리됩니다. 그러나 페이지 크기가 클 경우에는 조금 부정확할 수 있을 수 있습니다.
        * */
        //(방법1) long total = content.size();
        //(방법2)
        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
