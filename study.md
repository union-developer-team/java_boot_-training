day1.
[application-db.yml] 
ddl-auto: update > 엔티티 변경 시 테이블 보완(개발 추천)
ddl-auto: create > 재시작마다 테이블 새로 생성(초기 연습용)
show-sql: true > 실행 SQL 콘솔 출력(학습에 유용)

[entity > Member.java]
사용 어노테이션 기능 정리
1. @Entity 
- 역할: JPA 엔티티 선언
- 설명: 클래스를 DB를 연결하는 엔티티객체로 만들어 줌

2. @Table(name = "member")
- 역할: 테이블 이름 지정
- 설명: 연결한 DB에 생성할 테이블 이름을 지정

3. @Getter
- 역할: @Getter 자동 생성
- 설명: 데이터를 조회하는 메서드 생성

4. @Setter
- 역할: @Setter 자동 생성
- 설명: 데이터를 변경하는 메서드 생성

5. @ToString 
- 역할: 문자열 변환 메서드 생성
- 설명: 객체 정보를 문자열로 출력

6. @EntityListeners
- 역할: 엔티티 이벤트 감지
- 설명: 저장/수정 등 생명주기 이벤트 처리

7. @Id
- 역할: 기본키(PK) 지정
- 설명: 데이터를 구분하는 고유 식별자

8. @GenerateValue
- 역할: 기본키(PK) 값을 자동으로 생성하도록 지정
- 설명: JPA에게 기본키(PK) 값을 자동으로 생성하도록 지정하는 어노테이션입니다.

8-1. strategy
- 역할: 기본키 생성을 전략을 지정하는 속성
- 예시: @GeneratedValue(strategy = GenerationType.IDENTITY)

8-2. GenerationType.IDENTITY
- GenerationType 설명: JPA가 제공하는 기본키 생성 전략 열거형(Enum)
- IDENTITY 설명: DB의 자동 증가 기능을 사용하는 전략(MySQL AUTO_INCREMENT랑 동일)
- [그 외 GenerationType 전략 코드들]
- AUTO: JPA 구현체가 DB에 맞는 전략을 선택
- SEQUENCE: DB의 시퀀스 객체 사용
- TABLE: 별도의 키 생성용 테이블 사용

9. @Column
- 역할: Java 필드와 연결되는 데이터베이스 컬럼의 제약조건 등을 지정
- [주요 속성]
- nullable = false 설명: not null
- unique = true 설명: 중복 불가
- length = 100 설명: 최대 100자
- name = "email" 설명: DB 컬럼 이름 지정
- insertable = false 설명: INSERT 제외
- updatable = false 설명: UPDATE 제외

10. @CreatedDate
- 역할: 엔티티가 처음 저장되는 시점의 날짜를 자동으로 기록

11. @LastModifiedDate
- 역할: 엔티티가 마지막으로 수정된 시간 자동 기록

12. @Transient
- 역할: 해당 필드를 DB 컬럼으로 매핑하지 않음

13. @Enumerated
- 역할: Enum 타입을 DB 컬럼에 매핑

14. @OneToMany
- 역할: 일대다 관계 설정

15. @ManyToOne
- 역할: 다대일 관계 설정

16. @OneToOne
- 역할: 일대일 관계 설정

17. @JoinColumn
- 역할: 외래키(FK) 컬럼 설정

18. @NoArgsConstructor
- 역할: 기본 생성자 자동 생성(Lombok)

day2.
[entity > CommonCode.java]

1. @Table(name = "common_code", uniqueConstraints = @UniqueConstraint(columnNames = {"group_code", "common_code"}))
- 역할: 테이블 이름과 복합 유니크 제약 지정
- 설명: 테이블 이름은 common_code. group_code와 common_code 조합이 같으면 저장을 거부함
- columnName 에는 자바 필드명이 아니라 DB 컬럼명을 적음
- 예시: MENU + ITEM_LIST 는 한 번만 가능하고, JOB + ITEM_LIST 는 그룹이 달라서 가능

2. @PrePersist
- 역할: DB에 처음 저장되기 직전 메서드를 자동 호출
- 설명: initDefault()에서 useYn이 null이면 "Y"로 채움. 메서드 이름은 상관없음

3. 엔티티 객체
- 역할: 엔티티는 JPA가 관리하는 영속 객체이다. 
       @Entity가 붙은 클래스는 테이블과 매핑되고, 저장·조회·수정·삭제 대상이 된다.
       만약 엔티티를 수정하면 JPA가 그 변경을 감지해 트랜잭션이 끝날 때 SQL로 반영한다.
       그래서 엔티티는 Repository와 Service 내에서만 다루는 것이 맞다.

4. DTO 객체
- 역할: DTO는 요청과 응답을 옮기는 그릇이다. 테이블과 연결되자 않고, JPA가 추적하지 않는다.
       화면이나 API가 필요로 하는 값만 담으며 프로젝트의 용도에 따라 DTO가 더 갈라지기도 한다.
- 특징: DTO 객체는 별도 @Entity나 @Controller 처럼 DTO를 지정하는 어노테이션은 없다.