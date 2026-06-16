package com.wutao.dormItem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EnableJdbcRepositories(basePackages = "com.wutao.dormItem.repository")
@SpringBootApplication
public class DormitoryApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DormitoryApplication.class)
                .headless(false)
                .run(args);
    }

}
