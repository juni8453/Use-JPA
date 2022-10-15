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
    // Member (1) LAZY 로딩
    // Delivery (1) LAZY 로딩
    // orderItem.Item LAZY 로딩 + orderItem.getItem().~ LAZY 초기화(영속성 컨텍스트에 값이 없으니 쿼리발생) (2)
    // -> 총 6번의 쿼리 발생 (N + 1) 현재 Order 가 2개니 총 12번의 쿼리 발생
    @GetMapping("/api/v2/orders")
    public GlobalResponse<List<OrderDto>> orderV2() {
        List<OrderDto> dtos = orderRepository.findAll().stream()
            .map(order -> order.toOrderDto(order))
            .collect(Collectors.toList());

        return new GlobalResponse<>(1, dtos);
    }

    // select Order 시 LAZY 로딩 상태의 Entity 를 모두 Fetch Join 으로 끌고와서 한방에 처리하도록 성능 개선
    // 총 Order 는 2개, 각 Order 의 List<OrderItem> 은 2개 -> 즉 이 상태로 Fetch Join 시 Order 가 2개씩 조회되는 데이터 뻥튀기 현상 발생
    // (RDB 는 따로 List(다) 를 처리할 수 없으니까 List(다) 만큼 Row 가 증가되서 나오는 것)
    @GetMapping("/api/v3/orders")
    public GlobalResponse<List<OrderDto>> orderV3() {
        List<OrderDto> dtos = orderRepository.findAllWithItem().stream()
            .map(order -> order.toOrderDto(order))
            .collect(Collectors.toList());

        return new GlobalResponse<>(1, dtos);
    }

    // 중복 Order 를 제거하기 위해 distinct 키워드 사용
    // DB 의 distinct 와 다른 점으로 한 줄이 전부 같아야 중복제거를 해주는게 아닌, FK 값이 같다면 중복을 제거해준다.
    // Fetch Join + distinct 의 조합으로 중복이 제거된 SQL 한번이 나가도록 성능이 개선되었지만 페이징에서의 문제점이 하나 있다.
    // 일대다 Fetch Join 이 걸려있는 순간 데이터가 뻥튀기 되기 때문에 하이버네이트가 경고 로그를 남기면서 모든 데이터를 DB 에서 읽어오고,
    // 애플리케이션 메모리 자체에서 페이징 처리를 해버린다. (잘못하면 메모리 초과 에러로 이어진다.)
    // 또한 컬렉션 Fetch Join 은 하나만 사용하도록 하자. 두 개 이상 사용하면 데이터가 부정확하게 조회될 수 있다.
    @GetMapping("/api/v4/orders")
    public GlobalResponse<List<OrderDto>> orderV4() {
        List<OrderDto> dtos = orderRepository.findAllWithItemV4().stream()
            .map(order -> order.toOrderDto(order))
            .collect(Collectors.toList());

        return new GlobalResponse<>(1, dtos);
    }
}
