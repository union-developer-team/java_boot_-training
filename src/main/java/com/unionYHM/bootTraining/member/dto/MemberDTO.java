package com.unionYHM.bootTraining.member.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import com.unionYHM.bootTraining.member.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString 
public class MemberDTO {

    // Java Bean Validation 기능을 활용하여 입력값 검증 수행 => Validation 의존성(spring-boot-starter-validation) 추가 필요
	// => 다양한 어노테이션을 조합하여 복합 검증 조건 설정. 컨트롤러에서 바인딩 시점에 체크 가능
	// => 기본 문법 : @XXX(message = "오류 발생 시 표시할 메세지") 
	//    (필요에 따라 value 속성을 통한 값 지정도 가능)
	// @NotEmpty : 문자열이 null 이거나 길이가 0인 문자열(널스트링)을 허용하지 않음
	// @NotBlank : 문자열이 null 이거나 길이가 0인 문자열(널스트링) 또는 공백만 포함한 문자열을 허용하지 않음
	// @NotNull : 객체값 null 을 허용하지 않음
	// @Positive : 0 제외 양수만 허용(x > 0)
	// @PositiveOrZero : 0 이상의 양수만 허용(x >= 0)
	// @Negative : 0 제외 음수만 허용(x < 0)
	// @NegativeOrZero : 0 이하의 음수만 허용(x <= 0)
	// @Min(value) : 최소값 제한(x >= value)
	// @Max(value) : 최대값 제한(x <= value)
	// @Pattern(regexp = "정규표현식패턴문자열") : 특정 정규표현식에 매칭되는 문자열 탐색 후 일치하지 않을 경우 허용하지 않음
	// ---------------------------------------------------------------------------------------------------------------------
	private Long id;
	
	@NotBlank(message = "이메일은 필수 입력값입니다!")
	@Email(message = "이메일 형식에 맞게 입력해 주세요!")
	private String email;
	
	@NotBlank(message = "패스워드는 필수 입력값입니다!")
	@Length(min = 4, max = 16, message = "패스워드 4 ~ 16 자리 필수!")
	private String password;
	
	@NotBlank(message = "이름은 필수 입력값입니다!")
	@Length(min = 2, max = 20, message = "이름은 2 ~ 20 글자 필수!")
	private String name;
	
	@NotBlank(message = "우편번호는 필수 입력값입니다!")
	private String postCode; // 우편번호
	
	@NotBlank(message = "기본주소는 필수 입력값입니다!")
	private String address1; // 기본주소
	
	private String address2; // 상세주소(검증조건 없음)
	
	private LocalDate regDate; // 가입일자
	
	// --------------------------------------------------------
	// DTO <-> Entity 변환 메서드를 개발자가 직접 구현하지 않고 ModelMapper 라이브러리 활용하여 간편하게 구현 가능
	// => 단, 기본적으로 두 클래스간 동일한 필드명만 자동 변환 대상으로 취급됨
	private static ModelMapper modelMapper = new ModelMapper();
	
	// ModelMapper 객체의 map 메서드 활용하여 객체 변환 수행
	// 1) MemberDTO -> Member(엔티티) 타입으로 변환하는 toEntity 메서드 정의
	public Member toEntity() {
		// ModelMapper 객체의 map() 메서드 호출하여 원본 객체와 변환 대상 클래스 지정
		// 첫번째 파라미터 : 변환 전 원본 객체
		// 두번째 파라미터 : 변환할 대상 클래스(.class 포함)
		return modelMapper.map(this, Member.class); // 원본 객체 : this = 현재 객체 = MemberDTO
	}
	
	// 2) Entity -> DTO 로 변환하는 fromEntity 메서드 정의(파라미터로 엔티티 객체를 전달)
	// => 엔티티만 있고 DTO 객체가 없을 때 호출되어야 하므로 static 메서드로 선언
	public static MemberDTO fromEntity(Member member) {
		return modelMapper.map(member, MemberDTO.class);
	}

}
