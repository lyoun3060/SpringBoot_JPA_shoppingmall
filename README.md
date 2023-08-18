# SpringBoot_JPA_shoppingmall


    ※시큐리티 주의사항(23-08-08)

    스프링 버전5.7 이후로
    WebSecurityConfigurationAdapter는 이제 사장되어서 override하여 SecurityFilterChain을 사용할 수 없다
    따라서 직접 SecurityFilterChain을 Bean으로 설정하여 밑에 필요한 부분을 커스터마이징 하면 된다.


    스프링 시큐리티의 인증을 담당하는 AuthenticationManager는 이전 설정 방법임
    authenticationManagerBuilder를 이용해서 userDetailsService와 passwordEncoder를 설정했어야 했다.
    그러나 변경된 설정에서는 AuthenticationManager 빈을 생성하면, 생성시 스프링의 내부 동작으로 인해
    이전에는 작성해야 했던 UserSecurityService와 PasswordEncoder가 자동으로 설정됨.


    BCryptPasswordEncoder는 BCrypt 해시 알고리즘을 사용하여 비밀번호를 암호화하여 비밀번호를 보호한다.
    BCryptPasswordEncoder를 사용하여 암호화한 후, 해당 암호화된 비밀번호를 데이터베이스나 다른 저장소에 저장하여 사용자 인증에 활용된다.
    
    ------------------------------------------------------------------------------------------------------------------

    ※즉시로딩 <-> 지연로딩(23-08-11)

    지연 로딩(Asynchronous Loading)과 즉시 로딩(Synchronous Loading)
    웹 페이지나 애플리케이션에서 자원(이미지, 스크립트, 스타일 등)을 불러오는 방식을 나타내는 용어

    즉시로딩
    즉시 로딩은 페이지를 열 때 모든 자원을 순차적으로 로딩하는 방식
    
    지연로딩
    지연 로딩은 자원을 필요한 순간에 로드하는 방식
    사용자가 페이지를 열 때 모든 자원을 미리 다운로드하지 않고, 필요한 자원만 로딩하여 초기 페이지 로딩 속도를 높이는 전략
    지연로딩으로 설정하면 실제 엔티티에 넣는게 아닌, 프록시 객체를 넣어둠
    프록시 객체는 실제로 사용되기 전까지 데이터 로딩을 하지 않고, 실제사용시점에 조회쿼리문이 실행
             

    ------------------------------------------------------------------------------------------------------------------
    ※MVC - SpringMVC 차이
    MVC (Model-View-Controller):
    Model: 데이터 상태를 유지하고 조작하는 역할을 합니다.
    View: 데이터의 시각적 표현을 담당합니다.
    Controller: 모델과 뷰 사이의 상호 작용을 관리하며, 사용자 입력을 처리하고 모델을 업데이트하며, 변경된 데이터를 뷰에 반영합니다.

    Spring MVC (Spring Model-View-Controller):
    Spring 프레임워크 기반으로 구축된 웹 애플리케이션 아키텍처로, 기본적인 MVC 패턴을 웹 애플리케이션에 적용합니다.
    DispatcherServlet: 클라이언트 요청을 받아 컨트롤러에 전달하고, 컨트롤러가 처리한 결과를 보여줄 View를 결정하여 응답을 생성합니다.
    Controller: 사용자 입력을 처리하고 비즈니스 로직을 실행하며, 데이터를 모델에 저장하고 뷰를 선택합니다.
    Model: 데이터와 비즈니스 로직을 처리하는 역할을 합니다.
    View: 데이터를 시각적으로 표현하는 역할을 합니다. 다양한 뷰 템플릿 엔진을 지원

    ------------------------------------------------------------------------------------------------------------------
    
    ※ Querydsl(2023-08-15)
    @Query어노테이션을 이용하는 경우,JPQL(객체지향쿼리문-jpa에서 사용)을 사용하는데,
    이경우 sql문을 문자열(쿼리문)로 입력하기에 잘못입력해도 컴파일시 에러를 발견 할 수 없음
    Querydsl를 사용하면 쿼리문이 아닌 코드로 작성하여 컴파일러의 도움을 받을 수 있음
    
    - 장점1 : 고정된 쿼리문이 아닌 동적 쿼리 생성
    - 장점2 : 비슷한 쿼리를 재사용할 수 있음, 제약조건 조립 및 가독성 향상
    - 장점3 : 자바코드로 작성하여, 컴파일 시점에 오류발견가능

    QueryDSL을 사용하려면 프로젝트에 QueryDSL 라이브러리를 추가 + 엔티티 클래스와 Q타입 클래스를 생성필요
    Q타입 클래스는 QueryDSL의 쿼리 작성에 사용되며, 엔티티 클래스의 필드를 자동으로 매핑하여 제공
    Q가 붙는 클래스들을 자동으로 생성을 해주기 위하여 플러그인 및 의존성 기능사용 
    ㄴ> 메이븐 컴파일러 실행필요 = 의존성이 추가됨
    
    ------------------------------------------------------------------------------------------------------------------
    
    ※ fetchResult(), fetchCount()(2023-08-18)

    Querydsl 5.0부터 fetchResult()와 fetchCount()가 deprecated 됨
    사유는 모든 dialect에서 QueryResults로 count 쿼리를 날리는 것이 완벽하게 지원되지 않기 때문
    QueryResults<Item> resluts = queryFactory~~ -> List<Item> content = queryFactory~~
    fetchResults() -> fetch()
    fetchCount() -> fetch().size()

    (변경전-예시)
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    (변경후-예시)
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
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

        /*
        * 방법1과 방법2의 차이
        * (2)의 코드는 QueryDSL을 사용하여 DB에서 실제로 전체 아이템 수(total)를 조회하는 방식.
        *  Wildcard.count를 사용하여 SELECT 쿼리를 생성하고, fetchOne()을 호출하여 전체 아이템 수를 가져옴.
        *  이 방식은 실제 데이터베이스에서 전체 아이템 수를 가져오기 때문에 정확한 값을 반환.
        * (1)의 코드는 (2)의 방식과 달리 이미 페이지네이션된 콘텐츠(List<MainItemDto> content)의 크기를 사용하여 전체 아이템 수(total)를 구함.
        *  이 방식은 이미 쿼리를 실행하여 가져온 결과를 사용하므로 별도의 쿼리 실행 없이 처리됨. 그러나 페이지 크기가 클 경우에는 조금 부정확할 수 있을 수 있음.
        */
    }
    
    ------------------------------------------------------------------------------------------------------------------
    
    ※ @Querydsl과 @QueryProjection의 차이(2023-08-19)

    (요것)
    @Querydsl은 동적 쿼리 작성과 실행에 유용하며,
    @QueryProjection은 프로젝션 결과를 DTO로 변환하는 과정을 간단하게 처리해주는 역할

    1.@Querydsl
    @Querydsl 애노테이션은 QueryDSL을 사용하여 동적 쿼리를 작성할 때 메서드에 적용
    QueryDSL은 타입 안전한 쿼리 작성을 가능하게 해주는 라이브러리로, JPA 쿼리를 자바 코드로 작성하고 실행할 수 있도록 도와줌
    @Querydsl을 사용하여 메서드에 적용하면 해당 메서드의 파라미터를 활용하여 QueryDSL을 활용한 쿼리를 작성할 수 있음
    
    예)
    public class ItemRepositoryImpl implements ItemCustomRepository {

    @Override
    public List<Item> searchItems(String keyword) {
        QItem item = QItem.item;
        BooleanExpression keywordCondition = item.itemName.contains(keyword);

        return new JPAQuery<>(entityManager)
                .selectFrom(item)
                .where(keywordCondition)
                .fetch();
        }
    }

    2.@QueryProjection
    @QueryProjection은 QueryDSL에서 사용됨
    생성자에 @QueryProjection 애노테이션을 붙여서 해당 생성자를 QueryDSL 프로젝션에 활용할 수 있음.
    QueryDSL 프로젝션은 DTO로 데이터를 변환하거나 원하는 필드만 선택해서 가져오는 작업에 사용됨
    @QueryProjection을 사용하면 QueryDSL이 컴파일 시간에 생성한 Q타입과 함께 사용하여 쿼리의 프로젝션을 처리할 수 있음.

    예)
    public class ItemDto {

    private Long id;
    private String itemName;

    @QueryProjection
    public ItemDto(Long id, String itemName) {
        this.id = id;
        this.itemName = itemName;
        }

    // Getters and setters
    }

    
