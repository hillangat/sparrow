package com.techmaster.sparrow.config;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.data.DataLoaderService;
import com.techmaster.sparrow.repositories.SparrowDaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void setSparrowDaoFactoryContext () {
        if (SparrowDaoFactory.applicationContext == null) {
            SparrowDaoFactory.applicationContext = this.context;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", args.getOptionNames());
        logger.debug("Performing pre-start up processes");
        loadSparrowCache();
        executeDataLoaderService();
        logger.debug("Completed pre-start up processes!");
    }

    public void loadSparrowCache() {
        logger.debug("Loading sparrow cache...");
        SparrowCacheUtil.getInstance().loadHunterCache();
        logger.debug("Completed loading sparrow cache...");
    }

    public void executeDataLoaderService () {
        logger.debug("Executing data loader service...");
        DataLoaderService dataLoaderService = SparrowDaoFactory.getDaoObject(DataLoaderService.class);
        dataLoaderService.execute();
        logger.debug("Successfully executed data loader service...");
    }


}
