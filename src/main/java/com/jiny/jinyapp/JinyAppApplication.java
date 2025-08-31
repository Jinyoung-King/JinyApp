package com.jiny.jinyapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.jiny.jinyapp")
@EnableJpaAuditing // JPA가 자동으로 @CreatedDate를 인식하기 위하여 추가
public class JinyAppApplication {

    public static void main(String[] args) {
        log.info("JinyApp Application Started: {}", Arrays.toString(args));
        SpringApplication.run(JinyAppApplication.class, args);
    }

}
