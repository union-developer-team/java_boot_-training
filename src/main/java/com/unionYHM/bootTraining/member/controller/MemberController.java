package com.unionYHM.bootTraining.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unionYHM.bootTraining.member.dto.MemberDTO;
import com.unionYHM.bootTraining.member.dto.MemberLoginDTO;
import com.unionYHM.bootTraining.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/members")
@Log4j2
public class MemberController {

    // MemberService 객체 작동 주입
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // ==================================================
    // 로그인 폼 포워딩 시 아이디 기억하기 쿠키 사용 위해
    // 메서드 파라미터로 @CookieValue 어노테이션 활용
    @GetMapping("/login")
    public String loginForm(@CookieValue(value = "remember-id", required = false) String rememberId, Model model) {
        log.info(">>>>>>>>>>>> remember-id 쿠키값 : " + rememberId);

        // 쿠키값이 Model 객체에 추가
        model.addAttribute("rememberId", rememberId);

        return "member/member_login_form";
    }

    // ==================================================
    // 회원 가입
    // 가입 페이지 포워딩 시 입력값 검증에 사용할 DTO 객체를 Model 객체에 담아 함께 전달
    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/member_regist_form";
    }

    // POST 방식으로 요청되는 "/regist" 주소 매핑
    // => 폼 파라미터 값들을 MemberDTO 객체에 바인딩
    // 이 때, MemberDTO 객체에 바인딩되는 파라미터 값들이 대한 입력값 검증(Validation check) 수행을 위해
    // 메서드 선언부에 MemberDTO 타입 파라미터 선언 시 @Valid 어노테이션 적용
    // => 체크 결과를 뷰페이지에서 활용하기 위해 바인딩된 값이 대한 Model 객체 저장을 수행해줄 @ModelAttribute 어노테이션
    // 활용
    // => 전달된 파라미터들이 MemberDTO 객체에 바인딩되는 시점에 입력값 검증이 수행되고, 이 결과가 BindingResult 타입
    // 파라미터 저장됨
    @PostMapping("/regist")
    public String regist(@ModelAttribute("memberDTO") @Valid MemberDTO memberDTO, BindingResult bindingResult) {
        log.info(">>>>>>>>>>>>> memberDTO : " + memberDTO);

        // BindingResult 객체의 각 메서드 호출하여 입력값 검증 결과 얻어낼 수 있음
        log.info(">>>>>>>>>>>>> bindingResult.hasErrors : " + bindingResult.hasErrors()); // 검증 오류 발생 여부(true/false) 리턴
        log.info(">>>>>>>>>>>>> bindingResult.getAllErrors : " + bindingResult.getAllErrors()); // 전체 검증 결과에 대한 정보 리턴

        // 입력값 검증 경과가 true 일 때(= 검증 오류 발생 시) 다시 입력폼으로 포워딩
        if (bindingResult.hasErrors()) {
            // @ModelAttribute("memberDTO") 어노테이션에 의해 Model 객체 저장 없이도 뷰페이지에서 MemberDTO 객체 접근
            // 가능
            // BindingREsult 객체도 자동으로 뷰페이지로 전달됨
            return "member/member_regist_form";
        }

        // MemberService - registMember() 메서드 호출하여 회원가입 처리 요청
        // => 파라미터: MemberDTO 객체 리턴 타입: void
        memberService.regisMember(memberDTO);

        // 가입 성공 시 메인페이지로 리디렉션
        return "redirect:/";
    }

    // ==================================================
    // 회원 상세 정보 조회
    // 회원 상세 조회에 사용할 사용자명(이메일)을 별도로 전달받지 않고 컨트롤러 매핑 메서드 파라미터를 통해 사용자 인증 정보에 접근하여
    // 가져오기
    // => 1) org.springframwork.security.core.Authentication 객체를 주입받아 인증 정보에 접근
    // 2) @AuthenticationPrincipal 어노테이션 활용하여 UserDetails 객체(= MemberLoginDTO)를 주입받아
    // 인증 정보에 접근
    @GetMapping("/profile")
    public String showMemberProfile(Authentication authentication,
            @AuthenticationPrincipal MemberLoginDTO memberLoginDTO) {
        // import org.springframework.security.core.Authentication 객체는
        // SecurityContextHolder 객체로부터 얻을 수 있다.
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // => 메서드 파라미터로 Authentication 타입 객체 주입받아 사용 가능.
        log.info(">>>>>>>>>>>>>>>>>>>>> authentication.getName() : " + authentication.getName());
        // 사용자명(username = 현재 email)

        log.info(">>>>>>>>>>>>>>>>>>>>> authentication.getAuthorities() : " + authentication.getAuthorities());
        // 사용자 권한 목록

        log.info(">>>>>>>>>>>>>>>>>>>>> authentication.getDetails() : " + authentication.getDetails());
        // 상세정보(사용자 IP 주소, 세션 아이디)

        log.info(">>>>>>>>>>>>>>>>>>>>> authentication.getPrincipal() : " + authentication.getPrincipal());
        // 인증객체(UserDetails 또는 상속받아 구현한 구현체 = MemberLoginDTO)

        // 인증객체는 Authentication 객체의 getPrincipal() 메서드로 얻어올 수 있음(= UserDetails 타입)
        // => 이 객체를 MemberLoginDTO 객체로 다운캐스팅하여 사용도 가능
        // MemberLoginDTO memberLoginDTO =
        // (MemberLoginDTO)authentication.getPrincipal();
        // => 메서드 파라미터로 MemberLoginDTO 타입 객체 주입받아 사용 가능(@AuthenticationPrincipal 어노테이션
        // 필수!)
        log.info(">>>>>>>>>>>>>>>>>>>>> memberLoginDTO.getUsername : " + memberLoginDTO.getUsername());
        // 사용자명

        log.info(">>>>>>>>>>>>>>>>>>>>> memberLoginDTO.getAuthorities : " + memberLoginDTO.getAuthorities());
        // 사용자 권한 목록

        // ==================================================
        // 이메일(사용자명) 가져와서 변수에 저장
        String email = authentication.getName();
        return new String();
    }

}
