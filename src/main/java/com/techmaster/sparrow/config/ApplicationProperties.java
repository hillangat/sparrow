package com.techmaster.sparrow.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Component
@Data
public class ApplicationProperties {

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${sparrow.cross-origins-url")
    private String crossOriginsUrl;

    @Value("${dev.configs.build")
    private String applicationBuild;

    @Value("${sparrow.user_max_failed_count}")
    private int userMaxFailedCount;


    @PostConstruct
    public void validate() {
        Assert.notNull(adminUserName, "Admin user name is required by data loader");
    }

}
