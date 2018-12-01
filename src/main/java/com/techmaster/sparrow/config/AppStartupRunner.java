package com.techmaster.sparrow.config;

import com.techmaster.sparrow.cache.SparrowCache;
import com.techmaster.sparrow.cache.SparrowCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Override
    public void run(String...args) throws Exception {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
        logger.debug("Performing pre-start up processes");
        loadSparrowCache();
        logger.debug("Completed pre-start up processes!");
    }

    public void loadSparrowCache() {
        logger.debug("Loading sparrow cache...");
        SparrowCacheUtil.getInstance().loadHunterCache();
        logger.debug("Completed loading sparrow cache...");
    }


}
