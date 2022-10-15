package com.tany.jpamaster.api;

import com.tany.jpamaster.domain.Order;
import com.tany.jpamaster.domain.dto.response.GlobalResponse;
import com.tany.jpamaster.domain.dto.response.OrderDto;
import com.tany.jpamaster.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;

    // 그냥 이렇게 엔티티 직접 반환하면 안됨
    // 순환참조, API 스펙 변경 시 명세 깨짐, 유연함 등등 많은 문제
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    // select Order (1)
        // orderItem (1) LAZY 로딩
            // Item LAZY 로딩 + orderItem.getItem().~ LAZY 초기화(영속성 컨텍스트에 값이 없으니 쿼리발생) (2)
        // Member (1) LAZY 로딩
        // Delivery (1) LAZY 로딩
    // -> 총 6번의 쿼리 발생 (N + 1) 현재 Order 가 2개니 총 12번의 쿼리 발생
    @GetMapping("/api/v2/orders")
    public GlobalResponse<List<OrderDto>> orderV2() {
        List<OrderDto> dtos = orderRepository.findAll().stream()
            .map(order -> order.toOrderDto(order))
            .collect(Collectors.toList());

        return new GlobalResponse<>(1, dtos);
    }
}
