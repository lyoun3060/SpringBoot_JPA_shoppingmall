package com.shop.service;


import com.shop.entitiy.Member;
import com.shop.repository.MemberRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
//비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional을 선언하면, 로직을 처리하다 에러 발생시 변경된 데이터를 로직수행하기 이전상태로 콜백해줌
@Transactional
//@Bean을 주입하는 방법은 크게 2가지가 있다.
//1. @Autowired
//2. 필드주입(setter주입), 생성자 주입
@RequiredArgsConstructor
public class  MemberService implements UserDetailsService {

    // @RequiredArgsConstructor는 final이나 @NonNull이 붙은 필드에 생성자를 만들어줌,
    // 빈에 생성자가 하나이고 생성자의 파라미터타입이 빈으로 등록 가능하다면 2번방법으로 의존성 주입가능
    private final MemberRepositoy memberRepositoy;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepositoy.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepositoy.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 멤버입니다.");
        }
    }

    @Override
    //UserDetailsService 인터페이스의 loadUserbyUsername()메소드를 오저라이딩해
    //로그인할 유저의 email을 파라미터로 전달받음.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepositoy.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        //UserDetail을 구현하고 있는 User객체를 반환해줌
        //User객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role를 파라미터로 넘겨줌
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //loadUserByUsername 메서드는 사용자명을 기반으로 데이터베이스에서 사용자 정보를 조회
    //UserDetails 인터페이스를 구현한 객체를 반환함.
    //그리고 UserDetails 인터페이스를 구현한 객체는 사용자 정보를 나타내는 객체로, 일반적으로 User라는 클래스를 사용함.
    //User 클래스는 스프링 시큐리티가 제공하는 기본적인 사용자 정보를 저장하는 클래스임
    //User 클래스에는 (username), (password), (authorities) 등의 DB상의 정보가 포함되어 있음
}
