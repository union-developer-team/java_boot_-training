package com.unionYHM.bootTraining.member.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // JPA Entity 클래스임을 명시
@Table(name = "member") // DB 테이블과 매핑
@Getter // Getter 메서드 자동 생성
@Setter // Setter 메서드 자동 생성
@ToString   // toString() 메서드 자동 생성
@EntityListeners(AuditingEntityListener.class)  
// Auditing 기능을 사용하기 위해 EntityListeners 등록
// @EntityListeners(AuditingEntityListener.class)는
// 이 엔티티를 저장할 때, 날짜 같은 값을 자동으로 채워 주는 감시자를 붙여라는 뜻이다.
// 왜 필요한가? regDate에 @CreatedDate를 달아도, 그 자체만으로는 가입일이 자동으로 들어가지 않는다. 
// @CreatedDate는 "이 칸에 생성 시각을 넣어라"는 표시일 뿐이고, 
// 실제로 값을 넣는 주체는 AuditingEntityListener이다.
// [흐름도]
// 1. memberRepository.save(member)를 호출한다.
// 2. JPA가 DB에 insert하기 직전에 AuditingEntityListener를 실행한다.
// 3. 리스너가 @CreatedDate가 붙은 regDate에 현재 낧짜를 넣는다.
// 4. JPA가 DB에 insert한다.
public class Member {

    @Id // PK임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키(PK) 값을 자동으로 생성
    // GenerationType.IDENTITY : DB에 위임, MySQL의 AUTO_INCREMENT와 동일
    private Long id;

    @Column(nullable = false, unique = true) // not null, unique 제약 조건
    private String email;

    @Column(nullable = false, length = 100) // not null, 길이 제한
    @ToString.Exclude   // toString() 메서드에서 제외
    // @ToString.Exclude를 붙이지 않으면, toString() 메서드가 password까지 출력하게 된다.
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 10)
    private String postCode;

    @Column(nullable = false, length = 100)
    private String address1;

    @Column(nullable = false, length = 100)
    private String addresss2;

    @CreatedDate // 생성 시각을 자동으로 채워주는 어노테이션
    private LocalDate regDate;

}
