package com.greglturnquist.springagram.fileservice.s3.dummy.data.load;

import java.io.IOException;

import javax.annotation.PostConstruct;

import com.greglturnquist.springagram.fileservice.s3.model.User;
import com.greglturnquist.springagram.fileservice.s3.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityDetailsDummyLoader {

    private final UserRepository userRepository;

    @Autowired
    public SecurityDetailsDummyLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() throws IOException {

        userRepository.deleteAll();

        User greg = new User();
        greg.setName("greg");
        greg.setPassword("turnquist");
        greg.setRoles(new String[]{"ROLE_USER"});
        userRepository.save(greg);

        User roy = new User();
        roy.setName("roy");
        roy.setPassword("clarkson");
        roy.setRoles(new String[]{"ROLE_USER"});
        userRepository.save(roy);

        SecurityContextHolder.clearContext();
    }

}
