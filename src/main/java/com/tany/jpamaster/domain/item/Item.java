package com.tany.jpamaster.domain.item;

import com.tany.jpamaster.domain.Category;
import com.tany.jpamaster.exception.NotEnoughStockException;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 재고가 변경되는 비즈니스 로직 추가
     * DDD관점에서 엔티티 자체가 해결할 수 있는 로직은 엔티티 안에 넣는 것이 좋다.
     * stockQuantity를 item엔티티가 가지고 있기 때문이기도 하다.
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
