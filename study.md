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

day3.

1. JpaRepository를 상속한 인터페이스는 @Repository 없이도 Spring Data JPA가 구현 프록시를 생성하고 Bean으로 등록함. 

2. toEntity() 와 ModelMapperUtils.java 객체의 차이점
- 1) 화면에서 들어오는 정보는 DTO형태로 들어오며, 컨트롤러는 폼 값을 MemberDTO로 받는다.
예문: 
public String regist(@ModelAttribute("memberDTO") @Valid MemberDTO memberDTO, BindingResult bindingResult) {
	// ...
	memberService.registMember(memberDTO);

- 2) 서비스에서 그 DTO를 엔티티로 바꾼 뒤 저장한다.
예문: 
// MemberDTO 객체 -> Member 엔티티로 변환
Member member = memberDTO.toEntity();
// ...
Member resultMember = memberRepository.save(member);

3. ModelMapperUtils.java
- 설명: DTO와 엔티티를 서로 옮겨 담는 공통 변환기다.
       .toEntity() 처럼 클래스마다 변환 메서드를 두지 않고, 어떤 클래스 쌍이든 한곳에서 바꾸게 만든 모듈객체이다.

- 객체 구성:
1) ModelMapper(외부라이브러리): 
   역할: 필드를 복사하는 도구이고, 구성 메서드는 그 도구를 호출하는 입구이다.

2) convertObjectByMap(원본객체, 바꿀클래스)
   예시: convertObjectByMap(itemDTO, Item.class) -> 상품DTO를 엔티티로 변환

3) convertList(원본리스트, 바꿀클래스)
   예시: convertList(List<S> sourceList, Class<D> destinationClass) -> 상품 이미지 목록을 DTO로 변환

day4.

JPQL(Java Persistence Query Language)
- 설명: JPA 에서 제공하는 객체지향 쿼리 언어, 기존 SQL 문처럼 DB 테이블을 대상으로 하는 것이 아니라
       엔티티 객체 및 필드를 대상으로 쿼리 작성
       JPA 가 JPQL 을 해석하여 실제 DB 의 SQL 문장으로 변환하여 실행해준다

- 주의: 기본 문법 구조는 SQL 과 거의 동일하나, 테이블명 대신 엔티티명, 테이블의 컬럼명 대신 필드면 지정

- JPQL 과 함께 쓰는 어노테이션들:
- 1) @Query
- 설명: 메서드가 실행할 JPQL을 직접 적는다. 보통 Join이나 조회 컬럼을 지정할때 사용한다.
- 예문: @Query("SELECT m FROM Member m"
		+ " JOIN FETCH m.roles r"
		+ " JOIN FETCH r.role"
		+ " WHERE m.email = :email")

- 2) @Param
- 설명: :이름 과 메서드 파라미터를 연결한다. @Param("email")이 있어야 JPQL의 :email이 그 인자 
       값을 받는다.
- 예문: Optional<Member> findByEmailWithMemberRoles(@Param("email") String email);

- 3) @Modifying
- 설명: @Query에 UPDATE나 DELETE를 적을 때 함께 붙인다. 
       @Query만 있으면 JPA는 그 쿼리를 조회로 취급해서, 데이터를 바꾸는 구문은 실행할 수 없다.
- 예문: @Modifying
       @Query("""UPDATE MemberMenuOverride o
                 SET	   o.useYn = 'N'
                 WHERE  o.member.id = :memberId
                 AND	   o.menu.commonCode = :menuCode
                 AND	   o.menu.groupCode = 'MENU'""")
       void softDeleteOverride(@Param("memberId") Long memberId, @Param("menuCode") String menuCode);

@Service
- 역할: 서비스 빈 등록
- 설명: 이 클래스를 스프링이 관리하는 서비스 객체로 등록한다. 컨트롤러가 MemberService를 생성자로 받으면, 스프링이 이 객체를 만들어 넣어 준다.

@Log4j2
- 역할: 로그 객체 생성
- 설명: Lombok이 log라는 로그 객체를 만들어 준다. 그래서 필드에 Logger를 직접 선언하지 않고 log.info(...)를 쓸 수 있다. 
       회원 수정 완료 로그가 이 객체를 사용한다.

@Transactional
- 역할: 트랜잭션 경계 지정
- 설명: 메서드 시작부터 정상 종료까지를 하나의 작업 단위로 묶는다. 도중에 런타임 예외가 나면 그 메서드 안의 DB 변경을 되돌린다.

@Transactional(readOnly = true)
- 역할: 조회 전용 트랜잭션
- 설명: 위와 같은 트랜잭션이지만, 이 메서드는 데이터를 바꾸지 않는다는 표시다. 회원 조회처럼 find만 하는 메서드에 붙인다.

.findByEmail()
- 역할: 메서드 이름으로 조회 쿼리를 만든다
- 설명: JPA가 저장소 메서드 이름을 읽어 쿼리를 만든다. 
       findBy 뒤의 Email은 엔티티 필드명이며 호출하면 그 필드가 파라미터와 같은 행을 찾고, 없을 수 있으므로 Optional로 반환한다. 
       이름만 바꾸면 findByName()처럼 다른 조건의 조회가 된다.

.ifPresent()
- 역할: 값이 있을 때만 코드를 실행한다
- 설명: 자바 Optional의 메서드다. 
       Optional은 값이 있을 수도 있고 없을 수도 있는 상자를 나타내며, 
       상자 안에 값이 있으면 람다에 그 값을 넘겨 실행하고, 비어 있으면 아무것도 하지 않는다.

.orElseThrow()
- 역할: 값이 없으면 예외를 던지고, 있으면 값을 꺼낸다
- 설명: 자바 Optional의 메서드다. 
       비어 있으면 지정한 예외가 발생해 메서드가 끝나며, 값이 있으면 상자에서 꺼내 그 타입 그대로 반환한다. 
       결과가 반드시 있어야 다음 코드가 진행될 때 쓴다.

.encode()
- 역할: 비밀번호를 단방향으로 암호화한다
- 설명: 스프링 시큐리티 PasswordEncoder의 메서드다. 
       평문을 받아 복호화할 수 없는 암호문을 반환하며, BCryptPasswordEncoder로 만들면 같은 평문도 호출할 때마다 다른 암호문이 된다. 
       일치 여부는 암호문끼리 비교하지 않고, 인코더의 검증 메서드로 확인한다.

.toEntity()
- 역할: DTO를 엔티티 객체로 옮긴다
- 설명: 자바나 스프링이 제공하는 메서드가 아니다. 
       DTO에 직접 만든 변환 메서드이며, 화면이나 요청으로 받은 객체의 값을, 데이터베이스 저장에 쓰는 엔티티 객체로 복사해 새 엔티티를 반환한다.

.findByGroupCodeAndCommonCode()
- 역할: 필드 두 개가 모두 일치하는 대상을 조회한다
- 설명: findByEmail()과 같은 JPA 규칙이다. 
       And로 이어진 이름은 각 필드가 모두 맞아야 하는 조건이 된다. 반환 타입이 Optional이면 일치하는 데이터가 없을 수 있다는 뜻이다.

.addMemberRole()
- 역할: 엔티티의 연관 컬렉션에 항목을 추가한다
- 설명: 저장소 메서드가 아니다. 엔티티 안에 만든 도메인 메서드다. 
       연관된 객체를 만들어 엔티티가 가진 목록에 넣는다. 
       실제 INSERT는 이 메서드가 하지 않고, 이후 저장이 트랜잭션 안에서 실행될 때 반영된다.

.save()
- 역할: 엔티티를 데이터베이스에 반영한다
- 설명: Spring Data JpaRepository의 메서드다. 
       식별자가 없는 새 엔티티는 INSERT, 이미 관리 중인 엔티티는 변경 내용이 UPDATE로 반영된다. 저장된 엔티티를 반환한다.

.findById()
- 역할: 기본키로 한 건을 조회한다
- 설명: JpaRepository가 기본으로 제공하는 메서드다. 
       엔티티의 @Id 값으로 한 행을 찾고 Optional로 반환하며, 해당 키가 없으면 빈 Optional이다.

.convertObjectByMap()
- 역할: 객체 하나의 같은 이름 필드를 다른 클래스 객체로 복사한다
- 설명: 이 프로젝트의 ModelMapperUtils가 ModelMapper의 map()을 감싼 메서드다. 
       원본 객체와 변환할 클래스를 받으면, 필드 이름이 같은 값만 채워 새 객체를 반환한다. 
       단, 이름이 다른 필드는 복사되지 않는다.

.changeMember()
- 역할: 엔티티의 변경 가능한 필드만 수정한다
- 설명: 엔티티에 직접 만든 수정 메서드다. 
       파라미터로 받은 값 가운데 바꾸기로 정한 필드만 현재 엔티티에 덮어쓴다. 
       UPDATE 쿼리를 직접 실행하지 않으며, 트랜잭션 안의 영속 엔티티라면 메서드가 끝난 뒤 변경 감지에 의해 UPDATE가 나간다.

.selectMemberList()
- 역할: 매퍼에 연결된 SQL로 목록을 조회한다
- 설명: 마이바티스 매퍼 인터페이스의 메서드다. 
       JPA 쿼리 메서드와 달리 이름만으로 쿼리가 만들어지지 않고, XML이나 어노테이션에 적어 둔 SQL이 실행된다. 
       결과 행을 메서드의 반환 타입 목록으로 돌려준다.

.convertList()
- 역할: 리스트의 각 원소를 다른 타입으로 복사해 새 리스트를 만든다
- 설명: ModelMapperUtils의 메서드다. 
       원본 리스트를 돌면서 원소마다 map()과 같은 필드 복사를 하고, 그 결과를 List로 모은다. 변환 규칙은 convertObjectByMap()과 같다.