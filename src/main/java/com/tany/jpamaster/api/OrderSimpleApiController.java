package com.tany.jpamaster.api;

import com.tany.jpamaster.domain.Order;
import com.tany.jpamaster.domain.dto.response.GlobalResponse;
import com.tany.jpamaster.domain.dto.response.SimpleOrderDto;
import com.tany.jpamaster.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne 성능 최적화 ?
 * Order 를 조회
 * Order -> Member 연관 @ManyToOne
 * Order -> Delivery 연관 @OneToOne
 */
@RequiredArgsConstructor
@RestController
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // Entity 반환 시 StackOverFlow 예외 발생 (순환참조)
    // 양방향 매핑에서의 무한루프
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll();

        return orders;
    }

    // DTO 를 컬렉션 자체로 반환
    // API 스펙 변경 시 스펙이 꺠지는 문제 발생
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {

        // ORDER 2개 조회
        List<Order> orders = orderRepository.findAll();

        // 루프 한 번에 아래 쿼리 2개 실행
        // member.getName() -> LAZY 초기화로 MEMBER Table 조회
        // delivery.getAddress() -> LAZY 초기화로 DELIVERY Table 조회
        // 즉, 2번 루프가 돌아야하기 때문에 총 5번의 쿼리가 실행되는 문제 발생 (N + 1)
        // ORDER 조회 시 + N(회원 2번 + 배송 2번) 만큼 쿼리가 추가 실행되기 떄문.
        List<SimpleOrderDto> dtos = orders.stream()
            .map(Order::toDto)
            .collect(Collectors.toList());

        return dtos;
    }

    // DTO 를 껍데기 클래스에 담아서 반환
    // API 스펙 변경 시 유연하게 대처 가능 !
    @GetMapping("/api/v3/simple-orders")
    public GlobalResponse<List<SimpleOrderDto>> ordersV3() {

        // 역시 N + 1 문제 발생
        // LAZY 초기화 시 무조건 DB 에 쿼리를 보내는 것이 아니라 영속성 컨텍스트에 값이 있는지 먼저 확인하지만,
        // (예를 들면, A 와 B 가 따로 주문을 한 번씩 한게 아니라 A 혼자 2번 주문한 경우 N + 1 이 아닌 1 + 1)
        // (위의 예시대로라면 최초 A ORDER 조회 시 영속성 컨텍스트에 값이 저장되고 이후에는 영속성 컨텍스트에서 Member, Delivery 를 가져가므로)
        // 대부분의 경우 다른 유저도 주문을 하기 때문에 거의 N + 1 문제가 터진다고 보면 된다.
        List<SimpleOrderDto> dtos = orderRepository.findAll().stream()
            .map(Order::toDto)
            .collect(Collectors.toList());

        return new GlobalResponse<>(1, dtos);
    }


}
