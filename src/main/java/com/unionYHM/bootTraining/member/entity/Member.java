package com.unionYHM.bootTraining.member.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.unionYHM.bootTraining.common.entity.CommonCode;
import com.unionYHM.bootTraining.member.dto.MemberDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String postCode; // 우편번호

    @Column(nullable = false, length = 100)
    private String address1; // 기본주소

    @Column(nullable = false, length = 100)
    private String addresss2; // 상세주소

    // -----------------------------------------------------------------------
    // JPA 에서 특정 날짜 및 시각(또는 다른 형식도 가능) 필드값을 자동으로 생성하려면
    // JPA 감사(Auditing) 기능 사용을 위한 어노테이션 조합을 통해 자동 필드로 지정
    // => 자동 등록된 필드가 위치한 클래스(= 엔티티)와 스프링 메인 클래스(XXXApplication)에 어노테이션 추가 설정 필요
    @CreatedDate // 생성 시각을 자동으로 채워주는 어노테이션
    private LocalDate regDate; // 가입일자

    // -----------------------------------------------------------------------
    // 사용자(Member)와 사용자권한(CommonCode)는 다대다(N:M) 관계이므로
    // 완충 작용을 담당하는 MemberRole 엔티티를 추가로 정의하여 각각의 관계를 1:N 으로 풀어냄(Member 와 MemberRole 은 1:N 관계)
    // => Member 엔티티에서 @OneToMany 어노테이션으로 1:N 관계에서의 1에 해당하나는 엔티티 관계 지정
    // 1) mapperBy = "member" 속성: 현재 엔티티가 연관관계의 주인이 아니며, 이 때 상대방의 필드명을 지정하여 해당 필드를 기준으로 매핑 수행
    // 2) cascade = CascadeType.ALL 속성: 부모 엔티이가 저장/삭제될 경우 지식 엔티티도 저장/삭제
    // 3) orphanRemoval = true 속성: 부모 엔티티와 연관관계가 끊어진 자식 엔티티(= 고아객체) 자동으로 삭제
    @OneToMany (mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRole> roles = new ArrayList<>();

    // 사용자 권한을 추가하는 addMemberRole() 메서드 정의
    public void addMemberRole(CommonCode role) {
        // MemberRole 인스턴스 생성 시 생성자 파라미터로 현재 엔티티(Member)와 공통코드 엔티티(CommonCode) 전달
        MemberRole memberRole = new MemberRole(this, role);

        // 사용자 권한 목록 객체(List<MemberRole>)에 1개 권한 저장된 MemberRole 엔티티 추가
        roles.add(memberRole);
    }

    // -------------------------------------------------------------------------------------------
    // 회원 정보 수정(UPDATE)을 위한 메서드 추가 정의
    // => 파라미터로 전달받은 MemberDTO 에 저장된 이름, 비밀번호, 주소만 현재 엔티티 필드에 저장
    // => 이메일(로그인 아이디), 권한, 가입일자는 수정 대상이 아님
    // => 더티 체킹(Dirty Checking) 기능에 의해 변경 사항이 감지되면 트랜잭션 종료 시점에 DB에 변경사항 업데이트
    public void changeMember(MemberDTO memberDTO) {
        this.name = memberDTO.getName();
        this.password = memberDTO.getPassword();
        this.postCode = memberDTO.getPostCode();
        this.address1 = memberDTO.getAddress1();
        this.addresss2 = memberDTO.getAddress2();
    }

}
