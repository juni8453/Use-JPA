package com.tany.jpamaster.repository;

import com.tany.jpamaster.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
