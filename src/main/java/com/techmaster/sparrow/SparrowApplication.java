package com.techmaster.sparrow;

import com.techmaster.sparrow.repositories.SparrowBeanContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SparrowApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SparrowApplication.class, args);
        SparrowBeanContext.applicationContext = applicationContext;
    }
}
