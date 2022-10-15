package com.tany.jpamaster.domain.dto.response;

import com.tany.jpamaster.domain.Address;
import com.tany.jpamaster.domain.OrderStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    // 사실 DTO 안에 Entity 가 있으면 안된다. Entity 에 의존하기 때문에 API 스펙 변경 시 깨져버리게 된다.
    // private List<OrderItem> orderItems;

    private List<OrderItemDto> orderItems;
}
