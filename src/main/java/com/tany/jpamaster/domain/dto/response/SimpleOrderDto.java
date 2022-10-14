package com.tany.jpamaster.domain.dto.response;

import com.tany.jpamaster.domain.Address;
import com.tany.jpamaster.domain.Order;
import com.tany.jpamaster.domain.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class SimpleOrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
}
