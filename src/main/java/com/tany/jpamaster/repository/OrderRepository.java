package com.tany.jpamaster.repository;

import com.tany.jpamaster.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

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
