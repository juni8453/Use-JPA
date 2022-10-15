package com.tany.jpamaster.domain.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OrderItemDto {

    private String itemName;
    private int orderPrice;
    private int count;
}
