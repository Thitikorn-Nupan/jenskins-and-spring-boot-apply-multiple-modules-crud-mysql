package com.ttknp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// *** specify this spring boot app to scan app on "com.ttknp*" this path
// *** meaning all multiple modules that start with "com.ttknp*" can work well on this app if you do not , step inject won't work
// @ComponentScan(basePackages = {"com.ttknp"}) // *** if you gonna test (with mockito) you have to comment this annotation
@SpringBootApplication
public class ApiUsingSpringBootApplyCustomService3CrudMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUsingSpringBootApplyCustomService3CrudMysqlApplication.class, args);
    }

}
