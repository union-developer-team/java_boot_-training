package com.unionYHM.bootTraining.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unionYHM.bootTraining.common.entity.CommonCode;
import com.unionYHM.bootTraining.common.exception.DuplicateMemberException;
import com.unionYHM.bootTraining.common.repository.CommonCodeRepository;
import com.unionYHM.bootTraining.common.util.ModelMapperUtils;
import com.unionYHM.bootTraining.member.dto.MemberDTO;
import com.unionYHM.bootTraining.member.entity.Member;
import com.unionYHM.bootTraining.member.mapper.MemberMapper;
import com.unionYHM.bootTraining.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;

@Service 
@Log4j2 
public class MemberService {

    // MemberRepository 객체 자동 주입
    private final MemberRepository memberRepository;

    // CommonCodeRepository 객체 자동 주입
    private final CommonCodeRepository commonCodeRepository;

    // MemberMapper 객체 자동 주입
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, CommonCodeRepository commonCodeRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.commonCodeRepository = commonCodeRepository;
        this.memberMapper = memberMapper;
    }

    // ----------------------------------------------------------------------------
    // 회원 등록 요청
    @Transactional 
    public void regisMember(MemberDTO memberDTO){
        // 이메일로 사용자 조회 후 조회 결과가 있을 경우 예외 발생시키기(= 이메일 중복 체크)
        // => 주의! Optional 객체에 대한 orElseThrow() 메서드가 아닌 ifPresent() 메서드로 예외 처리 필요
        memberRepository.findByEmail(memberDTO.getEmail())
                .ifPresent(m -> {
                    throw new DuplicateMemberException("이미 가입된 이메일입니다!");
                });

        // 입력받은 비밀번호에 대한 단방향 암호화(평문 -> 암호문) 처리
        // => BCryptPasswordEncoder 객체의 encode 메서드 활용
        PasswordEncoder passwordEncode = new BCryptPasswordEncoder();
        memberDTO.setPassword(passwordEncode.encode(memberDTO.getPassword()));

        log.info(">>>>>>>>>>>>> 회원 정보 등록 : " + memberDTO);
        
        // MemberDTO 객체 -> Member 엔티티로 변환
        Member member = memberDTO.toEntity();
        log.info(">>>>>>>>>>>> 변환된 Member 엔티티 : " + member);

        // -------------------------------------------------------------------------
        // 회원 등록 시 기본적인 사용자 권한을 "일반 사용자(ROLE_USER)" 로 등록하기 위해 공통코드 테이블에서 일반 사용자의 권한 코드 조회하여 설정
        // 1) CommonCodeService - getMemberRole() 메서드 호출하여 사용자 권한 조회
        //    => 파라미터: 사용자 권한명("ROLE_USER") 리턴타입: CommonCode 엔티티
        // 과정 축소를 위해 CommonCodeRepository - findByGroupAndCommonCode() 메서드 호출
        // => 파라미터: 그룹 코드명("MEMBER_ROLE" = 고정), 사용자 권한명("ROLE_USER") 리턴타입: CommonCode 엔티티
        CommonCode commonCode = commonCodeRepository.findByGroupCodeAndCommonCode("MEMBER_ROLE","ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("해당 권한 코드 정보가 존재하지 않습니다!"));
        log.info(">>>>>>>>>>>>> 사용자 권한 코드 : " + commonCode);

        // 2) Member 엔티티에 공통코드(사용자 계정 유형) 추가
        member.addMemberRole(commonCode);
        log.info(">>>>>>>>>>>>> 사용자 권한 추가된 Member 엔티티 : " + member);

        // -------------------------------------------------------------------------
        // MemberRepository - save() 메서드 호출하여 DB 에 엔티티 정보 등록
        Member resultMember = memberRepository.save(member);

        if(resultMember == null) {
            throw new IllegalStateException("회원 등록 실패!");
        }
        
    }

    // ------------------------------------------------------------------------------
    // 사용자 상세정보 조회
    @Transactional (readOnly = true)
    public MemberDTO getMember(String email) {
        // --------------------------------------------------------------------------
        // MemberRepository - findByEmail() 메서드 호출하여 사용자 정보 조회(리턴타입: Member)
        // => 조회 결과가 없을 경우 "xxx 에 해당하는 회원 정보가 없습니다!" 예외 처리
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " 에 해당하는 회원 정보가 없습니다!"));
        // ------------------------------------------------------------------------------------------------------------------------------------------------------
        // Member 엔티티 - MemberRole 엔티티 - CommonCode 엔티티 사이의 연관관계 설정에 따라
        // Member 엔티티를 조회하더라도 MemberRole - CommonCode 에 해당하는 엔티티에 대한 조회는 즉시 수행되지 않는다! (= 지연 로딩 = Lazy Loading)
        // 즉, Member 엔티티만 조회하고, Member 내의 연관관계에 있는 엔티티들은 조회가 수행되지 않음
        // Hibernate: select m1_0.id,m1_0.address1,m1_0.address2,m1_0.email,m1_0.name,m1_0.passwd,m1_0.post_code,m1_0.reg_date from members m1_0 where m1_0.email=?
        // => members 테이블에 대한 조회만 수횅함
        // -------------------------------------------------------------------------------------------------------------------------------------------------------
        // 만약, 조회 결과 Member 엔티티에 접근할 경우 Member 엔티티에서 접근해야하는 MemberRole 엔티티가 존재해야하므로
        // Member 엔티티 접근 시점에 MemberRole 엔티티에 접근하기 위해 MemberRole 엔티티에 대한 조회도 수행된다.(= 지연 로딩 = Lazy Loading)
        // log.info(">>>>>>>>>>>> Member 엔티티 : " + member); 이 코드 실행 시점에 List<MemberRole> 에 대한 조회를 위해 member_role 테이블 조회가 수행됨 
        // Hibernate: select m1_0.id,m1_0.address1,m1_0.address2,m1_0.email,m1_0.name,m1_0.passwd,m1_0.post_code,m1_0.reg_date from members m1_0 where m1_0.email=?
		// Hibernate: select r1_0.member_id,r1_0.id,r1_0.member_role_id from member_role r1_0 where r1_0.member_id=?
        // => members 테이블 조회 수행된 후 다시 member_role 테이블에 대한 조회가 수행됨
        // --------------------------------------------------------------------------------------------------------------------------------------------------------
        // 만약, MemberRole 엔티티 내의 CommonCode 엔티티에 접근할 경우
        // 다시 CommonCode 엔티티에 대한 common_code 테이블 조회가 수행된다!
        // ex) List<MemberRole> roles 에 대한  for문을 적용하여 MemberRole 엔티티 내의 getRole() 메서드 호출 시 CommonCode 엔티티 리턴됨
        // for(MemberRole role : member.getRoles()) {
        //      log.info(">>>>>>>>>>>> CommonCode 엔티티 : " + role.getRole()); // 이 코드 실행 시점에 CommonCode 에 대한 조회를 위해 common_code 테이블 조회가 수행됨
        //}

        // --------------------------------------------------------------------------------------------------------------------------------------------------------
        // Member 엔티티 -> MemberDTO 객체로 변환하여 리턴
        // return MemberDTO.fromEntity(member)
        // --------------------------------------------------------------------------------------------------------------------------------------------------------
        // 모든 클래스산의 변환을 제공하는 사용자 정의 클래스 ModelMapperUtils 의 static 메서드 convertObjectByMap() 활용하여 동일한 변환 수행
        return ModelMapperUtils.convertObjectByMap(member, MemberDTO.class);
    }

    // 회원 번호로 상세정보 조회
    @Transactional (readOnly = true)
    public MemberDTO getMember(Long memberId){
        // MemberRepository - findById() 메서드 호출하여 사용자 정보 조회(리턴타입: Member)
        // => 조회 결과가 없을 경우 "xxx 에 해당하는 회원 정보가 없습니다!" 예외 처리
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 없습니다!"));
        // --------------------------------------------------------------------------------------------------------------------------------------------------------
        return ModelMapperUtils.convertObjectByMap(member, MemberDTO.class);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------
    // 로그인 사용자 정보 수정
    // => 이메일은 로그인 아이디이므로 변경하지 않음(Member.changeMember 도 이메일은 반영하지 않음)
    // => 화면에서 입력받은 비밀번호는 평문이므로 encode 후 저장
    //    조회 시 꺼내 둔 BCrypt 암호문을 다시 encode 하면 로그인이 불가능해지므로 평문만 암호화
    @Transactional 
    public void updateMember(String loginEmail, MemberDTO memberDTO){
        // MemberRepository - findByEmail() 로 로그인 사용자 조회
        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(() -> new UsernameNotFoundException(loginEmail + " 에 해당하는 회원 정보가 없습니다!"));
        
        // 입력받은 비밀번호에 대한 단방향 암호화(평문 -> 암호화) 처리
        // => 회원 등록(regisMember)과 동일하게 BCryptPasswordEncoder 사용
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        // Member 엔티티에 수정값 반영(더티 체킹으로 UPDATE)
        member.changeMember(memberDTO);
        log.info(">>>>>>>>>>>>>> 회원 정보 수정 완료 - email : "  + loginEmail);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------
    // 임시) 마이바티스 매퍼 동작 환인용 메서드
    public List<MemberDTO> getMemberListForMapper() {
        return ModelMapperUtils.convertList(memberMapper.selectMemberList(), MemberDTO.class);
    }
}
