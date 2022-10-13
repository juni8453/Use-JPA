package com.tany.jpamaster.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GlobalResponse<T> {

    private Integer code;
    private T data;
}
