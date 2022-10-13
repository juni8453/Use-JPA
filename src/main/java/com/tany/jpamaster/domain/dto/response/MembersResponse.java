package com.tany.jpamaster.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MembersResponse {

    private int userCount;
    private List<MemberResponse> members;
}
