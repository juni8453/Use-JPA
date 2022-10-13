package com.tany.jpamaster.api;

import com.tany.jpamaster.domain.Member;
import com.tany.jpamaster.domain.dto.request.CreateMemberRequest;
import com.tany.jpamaster.domain.dto.request.UpdateMemberRequest;
import com.tany.jpamaster.domain.dto.response.*;
import com.tany.jpamaster.service.MemberService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        // 원래는 Controller 에서 이렇게 Entity 를 만드는 것은 좋지 않고, 서비스 단으로 DTO 를 넘긴 뒤,
        // 해당 Entity 의 빌더 생성자나 정적 팩토리 메서드를 통해 Entity 를 만들고 DB 에 저장해야 한다. (예제니까 패스)
        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(request.getAddress());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    // 멱등성을 가진 PUT 을 수정 Method 로 가져간다.
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        // Update 는 수정 커맨드이기 때문에 여기서 Entity 관련 데이터를 반환받는게 아니라,
        // 수정은 딱 수정만 하도록 하고, 클라이언트로 ID 를 반환할 때는 조회 커맨드를 따로 사용하는 것이 유지보수성을 높일 수 있는 기법 중 하나다 !
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v3/members")
    public GlobalResponse<MembersResponse> memberV3() {
        MembersResponse findMembers = memberService.getMembers();

        return new GlobalResponse<>(1, findMembers);
    }
}
