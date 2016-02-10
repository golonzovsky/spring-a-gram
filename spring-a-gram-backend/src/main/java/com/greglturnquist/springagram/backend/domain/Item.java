package com.greglturnquist.springagram.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@ToString(exclude = "gallery")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private String image;

    @ManyToOne
    private Gallery gallery;

    @JsonIgnore
    @OneToOne
    private User user;

    /**
     * TODO: Lombok generated some error inside IntelliJ. Only solution was to hand write this setter.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

}
