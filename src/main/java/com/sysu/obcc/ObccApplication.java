package com.sysu.obcc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.sysu.obcc.http.dao")
@SpringBootApplication
@EnableTransactionManagement

public class ObccApplication {

    public static void main(String[] args) {

        SpringApplication.run(ObccApplication.class, args);

    }

}
