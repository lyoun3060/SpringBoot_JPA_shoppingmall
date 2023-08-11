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

    

    

    
