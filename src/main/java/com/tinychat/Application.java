package com.tinychat;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.bannerMode(Banner.Mode.CONSOLE)
                .sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }

}
