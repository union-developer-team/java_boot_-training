package com.unionYHM.bootTraining.member.mapper;

import java.util.List;

import com.unionYHM.bootTraining.member.entity.Member;

/**
 * MemberMapper
 */
public interface MemberMapper {

    List<Member> selectMemberList();

}
