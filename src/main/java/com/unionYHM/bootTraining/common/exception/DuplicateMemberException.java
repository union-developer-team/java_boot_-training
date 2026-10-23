package com.unionYHM.bootTraining.common.exception;

// 이미 존재하는 사용자에 대한 예외 클래스
public class DuplicateMemberException extends RuntimeException{

    public DuplicateMemberException() {
        super("이미 가입된 회원입니다.");
    }

    public DuplicateMemberException(String message) {
        super(message);
    }

}
