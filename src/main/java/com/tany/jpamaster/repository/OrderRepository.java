package com.tany.jpamaster.repository;

import com.tany.jpamaster.domain.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Order 에 LAZY 로딩으로 걸려있는 Entity 인 Member, Delivery 를 한 번에 가져오게 해서 N + 1 예방
    @Query("select o from Order o join fetch o.member m join fetch o.delivery d")
    List<Order> findAllWithMemberDelivery();

    @Query("select o from Order o " +
        "join fetch o.member m " +
        "join fetch o.delivery d " +
        "join fetch o.orderItems oi " +
        "join fetch oi.item i")
    List<Order> findAllWithItem();

    @Query("select distinct o from Order o " +
        "join fetch o.member m " +
        "join fetch o.delivery d " +
        "join fetch o.orderItems oi " +
        "join fetch oi.item i")
    List<Order> findAllWithItemV4();
}
