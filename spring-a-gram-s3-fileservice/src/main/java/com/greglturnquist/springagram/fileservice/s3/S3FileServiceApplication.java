package com.greglturnquist.springagram.fileservice.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableEurekaClient
//@EnableRedisHttpSession
//@EnableAutoConfiguration(exclude={RedisAutoConfiguration.class, SessionAutoConfiguration.class})
public class S3FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(S3FileServiceApplication.class, args);
    }
}
