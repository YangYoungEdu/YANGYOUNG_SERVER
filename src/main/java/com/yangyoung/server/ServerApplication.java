package com.yangyoung.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class ServerApplication {

    public static void main(String[] args) {
        // TimeZone 설정
        LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        SpringApplication.run(ServerApplication.class, args);
    }

}
