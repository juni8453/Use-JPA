package com.tany.jpamaster.repository;

import com.tany.jpamaster.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PageOrderRepository {

    private final EntityManager em;

    // Order 과 ToOne 관계의 Member, Delivery 엔티티는 모두 Fetch Join
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }
}
