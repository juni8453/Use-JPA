package com.tany.jpamaster.domain.dto.response;

import com.tany.jpamaster.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberResponse {

    private Long id;
    private String name;
    private Address address;
}
