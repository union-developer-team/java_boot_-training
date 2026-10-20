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