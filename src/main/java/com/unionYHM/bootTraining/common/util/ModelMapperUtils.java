package com.unionYHM.bootTraining.common.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

// A 클래스와 B 클래스간의 상호 변환을 수행할 클래스
// => ModelMapper 라이브러리를 활용하여 모든 클래스 간의 일치하는 필드를 상호 변환하는 공통 모듈 정의
public class ModelMapperUtils {
    public static final ModelMapper MODEL_MAPPER = new ModelMapper();

    // A 객체와 B클래스를 전달받아 A 객체를 B 클래스 타입 객체로 변환하여 리턴하는 convertObjectByMap 메서드 정의
    // ex) convertObjectByMap(member, MemberDTO.class) => Member 엔티티를 전달받아 MemberDTO 타입 객체로 변환하여 리턴
    // ex) convertObjectByMap(item, ItemDTO.class) => Item 엔티티를 전달받아 ItemDTO 타입 객체로 변환하여 리턴
    // => 메서드 호출 시 매번 전달되는 엔티티 객체와 클래스 타입에 대한 제네릭을 적용하기 위해 메서드 단위 제네릭타입을 지정
    //    (메서드 선언 시 리턴타입 앞에 메서드에서 사용할 제네릭 타입을 <> 내부에 지정)
    
    // ------------------------------------------------------------------------------------------------------
    // 임시) Member 엔티티 -> MemberDTO 타입 객체로 변환하는 메서드 정의
    // public static Member convertObjectByMap(Member member, Class<Member> memberDTOClass) {
    //     return MODEL_MAPPER.map(member, memberDTOClass);
    // }

    // ------------------------------------------------------------------------------------------------------
    // 첫번째 파라미터: Object 객체
    // 두번째 파라미터: 변환할 클래스 객체 => 제네릭타입 D(Destination)로 표기
    // => 리턴타입: 두번째 파라미터로 전달받은 타입(D)
    // public static <D> D convertObjectByMap(Object sourceObject, Class<D> destinationClass) {
    //     return MODEL_MAPPER.map(sourceObject, destinationClass);
    // }

    // ------------------------------------------------------------------------------------------------------
    // 첫번째 파라미터: 엔티티 객체 => 제네릭타입 S(Source) 로 표기
    // 두번째 파라미터: 변환할 클래스 객체 => 제네릭타입 D(Destination)로 표기
    // => 리턴타입: 두번째 파라미터로 전달받은 타입(D)
    public static <S, D> D convertObjectByMap(Object sourceObject, Class<D> destinationClass) {
        return MODEL_MAPPER.map(sourceObject, destinationClass);
    }

    // ------------------------------------------------------------------------------------------------------
    // List<A> -> List<B> 타입으로 변환하는 컬렉션 매핑 메서드
    // 첫번째 파라미터: List<엔티티> 객체 => 제네릭타입 S(Source) 로 표기
    // 두번째 파라미터: 변환할 클래스 객체 => 제네릭타입 D(Destination)로 표기
    public static<S, D> List<D> convertList(List<S> sourceList, Class<D> destinationClass) {
        // 1) List<ItemImg> 객체에 대한 자바 스트림 생성
        return sourceList.stream()
                // 2) 생성된 스트림 내의 각 요소에 존재하는 fromEntity() 메서드를 호출하여 ItemImg -> ItemImgDTO 객체로 변환
                .map(entity -> MODEL_MAPPER.map(entity, destinationClass))
                // 3) ItemImgDTO 객체를 List 에 담기
                .collect(Collectors.toList());
    }

}
