package com.tany.jpamaster.service;


import lombok.RequiredArgsConstructor;

import com.tany.jpamaster.domain.Member;
import com.tany.jpamaster.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member) {
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void update(Long id, String name) {
        Member findMember = findOne(id);

        findMember.setName(name);
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("해당 유저는 존재하지 않습니다."));
    }
}
