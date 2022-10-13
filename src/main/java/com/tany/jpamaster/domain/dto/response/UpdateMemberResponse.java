package com.tany.jpamaster.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateMemberResponse {

    private Long id;
    private String name;
}
