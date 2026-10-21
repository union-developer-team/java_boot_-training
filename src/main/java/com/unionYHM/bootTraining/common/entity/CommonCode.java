package com.unionYHM.bootTraining.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 공통코드를 관리하는 엔티티
@Entity
// 테이블 제약조건 설정
// => 두 개의 컬럼 조합이 유니크인 조건 설정(그룹코드(group_code) + 공통코드(common_code) 컬럼 = 유니크이면 OK))
@Table (name = "common_code", uniqueConstraints = @UniqueConstraint (columnNames = {"code_group", "common_code"})) 
@Getter 
@Setter 
@ToString 
public class CommonCode {

    @Id // PK임을 명시
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String id;   // 공통코드번호

    @Column (nullable = true, length = 20)
    private String groupCode;   // 상위공통코드(= 공통코드 그룹)

    @Column (name = "common_code", nullable = false, length = 50)
    private String commonCode; // 공통코드(하위 = 실제 사용하는 공통코드)

    @Column (name = "common_code_name", nullable = false, length = 20)
    private String commonCodeName; // 공통코드명(코드를 한국어로 표기할 컬럼)

    private String description; // 공통코드 설명

    @Column (name = "use_yn", nullable = false, length = 1)
    private String useYn; // 공통코드 사용여부(사용:Y, 미사용:N)

    // 엔티티 저장 전 useYn 값이 null 이면 기본값 Y 로 설정(기본값 보정 가능)
    @PrePersist // 엔티티가 저장되기 전에 실행되는 콜백 메서드
    public void initDefault() {
        if (useYn == null) useYn = "Y";
        // 나머지 항목들도 기본값 보정이 필요한 경우 if 문으로 추가
    }
}
