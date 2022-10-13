package com.tany.jpamaster;

import com.tany.jpamaster.domain.*;
import com.tany.jpamaster.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * userA
 *  JpaBook1
 *  JpaBook2
 * userB
 *  SpringBook1
 *  SpringBook2
 */

@RequiredArgsConstructor
@Component
public class InitDb {

    private final InitService initService;

    // 스프링 동작 시점에 dbInit() 호출
    @PostConstruct
    public void init() {
        initService.dbInitA();
        initService.dbInitB();
    }


    @RequiredArgsConstructor
    @Transactional
    @Component
    static class InitService {
        private final EntityManager em;

        public void dbInitA() {
            Member member = createMember("userA", "서울", "동작구", "11111");
            em.persist(member);

            Book jpaBook1 = createBook("JpaBook1", 10000, 100);
            Book jpaBook2 = createBook("JpaBook2", 20000, 100);
            em.persist(jpaBook1);
            em.persist(jpaBook2);

            OrderItem orderItem1 = OrderItem.createOrderItem(jpaBook1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(jpaBook2, 20000, 2);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInitB() {
            Member member = createMember("userB", "진주", "가호동", "22222");
            em.persist(member);

            Book springBook1 = createBook("SpringBook1", 20000, 200);
            Book springBook2 = createBook("SpringBook2", 40000, 300);
            em.persist(springBook1);
            em.persist(springBook2);

            OrderItem orderItem1 = OrderItem.createOrderItem(springBook1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(springBook2, 40000, 4);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));

            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);

            return book;
        }

        private Delivery getDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}


