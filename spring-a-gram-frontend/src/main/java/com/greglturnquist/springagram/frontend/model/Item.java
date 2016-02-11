package com.greglturnquist.springagram.frontend.model;

import lombok.Data;

@Data
public class Item {

    private String image;
    private Gallery gallery;
    private User user;

}
