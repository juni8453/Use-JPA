package com.tany.jpamaster.domain.dto.request;

import com.tany.jpamaster.domain.Address;
import lombok.Data;

@Data
public class UpdateMemberRequest {
    private String name;
    private Address address;
}
