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

    
    
