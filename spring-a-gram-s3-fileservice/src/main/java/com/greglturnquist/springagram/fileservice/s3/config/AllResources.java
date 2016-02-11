package com.greglturnquist.springagram.fileservice.s3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by golonzovsky on 2/11/16.
 */
@Configuration
class AllResources extends WebMvcConfigurerAdapter {

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseSuffixPatternMatch(false);
    }

}
