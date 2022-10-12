package com.tany.jpamaster.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;

@Setter
@Getter
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
    private String etc;
}
