package com.greglturnquist.springagram.fileservice.s3.service;

import com.greglturnquist.springagram.fileservice.s3.model.User;
import com.greglturnquist.springagram.fileservice.s3.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SpringDataJpaUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SpringDataJpaUserDetailsService.class);

    private UserRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching user " + username);
        User user = repository.findByName(username);
        log.info("Transforming " + user + " into UserDetails object");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles()));
        log.info("About to return " + userDetails);
        return userDetails;
    }
}
