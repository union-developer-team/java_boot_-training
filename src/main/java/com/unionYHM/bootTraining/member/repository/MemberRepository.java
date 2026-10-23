package com.unionYHM.bootTraining.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unionYHM.bootTraining.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

    // 이메일로 사용자 조회(기본 메서드가 아니므로 정의 필요)
    Optional<Member> findByEmail(String email);

    // [ JPQL(Java Persistence Query Language) ]
    // - JPA 에서 제공하는 객체지향 쿼리 언어
    // - 기존 SQL 문처럼 DB 테이블 대상으로 하는 것이 아니라, 엔티티 객체 및 필드를 대상으로 쿼리 작성
    // - JPA 가 JPQL 을 해석하여 실제 DB 의 SQL 문장으로 변환하여 실행해준다!
    // - 주의! 기본 문법 구조는 SQL 과 거의 동일하니, 테이블명 대신 엔티티명, 테이블의 컬럼명 대신 엔티티의 필드명 지정

    // Member 엔티티와 연관관계에 있는 MemberRole 에서 LAZY 로딩에 의한 로딩 문제를 해결하는 방법
    // email 을 기준으로 Member 엔티티의 role 에 해당하는 MemberRole 엔티티의 CommonCode 엔티티(role)까지도 JOIN 해야함
    @Query("SELECT m FROM Member m"      // FROM 절 뒤에 테이블명 member 가 아닌 엔티티명 Member 로 지정
           + " JOIN FETCH m.roles r"    // Member 엔티티의 roles 컬랙션에 해당하는 MemberRole 엔티티를 즉시 로딩(= EAGER)하여 가져오기 위한 JOIN(즉, JOIN FETCH 는 연관된 엔티티까지 한번에 SELECT)
           + " JOIN FETCH r.role"       // 중간 엔티티에 해당하는 MemberRole 내부의 CommonCode 엔티티를 다시 JOIN 해서 가져오기
           + " WHERE m.email = :email") // Member 엔티티의 email값(m.email)이 메서드 파라미터로 전달된 email(:email)과 같은 조건 설정
    // 이 때, :email 로 지정한 email 파라미터를 JPQL 에서 접근하기 위행 @Param 어노테이션을 적용하여 파라미터명 지정
    Optional<Member> findByEmailWithMemberRole(@Param ("email") String email); 

}
