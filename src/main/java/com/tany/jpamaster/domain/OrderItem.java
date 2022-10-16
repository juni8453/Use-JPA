package com.tany.jpamaster.domain;

import com.tany.jpamaster.domain.item.Item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    private OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(item, orderPrice, count);
        item.removeStock(count);

        return orderItem;
    }

    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
