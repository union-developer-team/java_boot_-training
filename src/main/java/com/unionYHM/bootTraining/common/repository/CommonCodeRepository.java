package com.unionYHM.bootTraining.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unionYHM.bootTraining.common.entity.CommonCode;

/**
 * CommonCodeRepository
 */
@Repository // 공통코드 Repository 인터페이스
public interface CommonCodeRepository extends JpaRepository<CommonCode, String>{
    // JpaRepository 를 상속받아 CommonCode 엔티티에 대한 CRUD 기능을 제공
    // JpaRepository<엔티티 클래스, PK 타입> 형식으로 지정

    // 특정 공통코드 조회
    Optional

}
