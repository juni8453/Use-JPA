package com.tany.jpamaster.domain;

import com.tany.jpamaster.domain.dto.response.MemberResponse;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public MemberResponse toDto() {
        return new MemberResponse(id, name, address);
    }
}
