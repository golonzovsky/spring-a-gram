package com.greglturnquist.springagram.fileservice.mongodb;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@ToString(exclude = "password")
public class User implements Serializable {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    private String id;

    private String name;

    // This field MUST be protected against any form of
    // serialization to avoid security leakage
    @JsonIgnore
    private String password;

    private String[] roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

}
