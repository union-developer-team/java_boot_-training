package com.unionYHM.bootTraining.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@ToString 
public class CommonCodeDTO {

    private String id;; // 공통코드번호
    private String groupCode; // 상위공통코드(= 공통코드 그룹)
    private String commonCode; // 공통코드(하위 = 실제 사용하는 공통코드)
    private String commonCodeName; // 공통코드명(코드를 한국어로 표기할 컬럼)
    private String description; // 공통코드 설명
    private String useYn; // 공통코드 사용여부(사용:Y, 미사용:N)

}
