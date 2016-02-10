package com.greglturnquist.springagram.backend.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "owner", types = Item.class)
public interface Owner {

    User getUser();

    String getImage();

}
