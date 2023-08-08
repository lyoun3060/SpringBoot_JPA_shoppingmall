package com.shop.entitiy;


import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Data
@ToString
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // JPA에서 기본키 생성전략을 AUTO로 하는것
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String address;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        //스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean 을 파라미터로 넘겨서 비밀번호를 암호화함
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member; //->Member엔티티를 DB에 저장할 수 있도록 MemberRepository를 만들자
    }

}
