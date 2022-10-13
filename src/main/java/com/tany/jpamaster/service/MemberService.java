package com.tany.jpamaster.service;


import com.tany.jpamaster.domain.dto.response.MemberResponse;
import com.tany.jpamaster.domain.dto.response.MembersResponse;
import lombok.RequiredArgsConstructor;

import com.tany.jpamaster.domain.Member;
import com.tany.jpamaster.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public MembersResponse getMembers() {
        List<MemberResponse> memberResponses = memberRepository.findAll().stream()
            .map(Member::toDto)
            .collect(Collectors.toList());

        int userCount = memberResponses.size();

        return new MembersResponse(userCount, memberResponses);
    }
}
