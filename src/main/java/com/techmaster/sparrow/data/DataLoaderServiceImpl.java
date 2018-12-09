package com.techmaster.sparrow.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.entities.DataLoaderConfig;
import com.techmaster.sparrow.entities.MediaObj;
import com.techmaster.sparrow.entities.User;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.enums.FileTypeEnum;
import com.techmaster.sparrow.imports.extraction.ExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractorFactory;
import com.techmaster.sparrow.repositories.*;
import com.techmaster.sparrow.services.UserService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class DataLoaderServiceImpl implements DataLoaderService {

    @Autowired private DataLoaderConfigRepository configRepository;
    @Autowired private EmailTemplateRepository emailTemplateRepository;
    @Autowired private UserService userService;
    @Autowired private EmailReceiverRepo emailReceiverRepo;
    @Autowired private EmailAttachmentRepo emailAttachmentRepo;
    @Autowired private MediaObjRepo mediaObjRepo;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @PostConstruct
    public void validate() {
        Assert.notNull(adminUserName, "Admin user name is required by data loader");
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute() {

        List<DataLoaderConfig> dataLoaderConfigs = getDataLoaderConfigs();
        saveDataLoaderConfigs(dataLoaderConfigs);

        dataLoaderConfigs.forEach(c -> {
            Workbook workbook = getWorkBook(c);
            if (workbook != null) {
                String originalFileName = SparrowUtil.getOrifinalFileNameForPath(c.getFileLocation());
                ExcelExtractor excelExtractor = ExcelExtractorFactory.getExtractor(c.getExtractor(),workbook, adminUserName, originalFileName);
                excelExtractor.execute();
            }
        });

        createUser();
        List<MediaObj> mediaObjs = loadMediaObjects();
        loadEmailTemplates(mediaObjs);
        loadEmailReceivers();
    }

    @Override
    public List<DataLoaderConfig> getDataLoaderConfigs() {

        logger.debug("Loading data loader configs...");

        List<DataLoaderConfig> configs = new ArrayList<>();
        File file = new File(SparrowURLConstants.DATA_LOAD_CONFIG_JSON);
        if (file != null && file.exists()) {
            String fileStr = SparrowUtil.getStringOfFile(file);
            JSONArray jsonArray = new JSONArray(fileStr);
            if (jsonArray.length() > 0) {
                for( int i = 0; i < jsonArray.length(); i++ ) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        DataLoaderConfig config = mapper.readValue(jsonArray.getJSONObject(i).toString(), DataLoaderConfig.class);
                        SparrowUtil.addAuditInfo(config, adminUserName);
                        configs.add(config);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return configs;
    }

    @Override
    public Workbook getWorkBook(DataLoaderConfig config) {

        logger.debug("Loading workbooks...");

        if (config != null) {
            FileTypeEnum fileType = config.getFileType();
            if (FileTypeEnum.EXCEL.equals(fileType)) {
                String fileLoc = SparrowURLConstants.RESOURCE_BASE_PATH + config.getFileLocation();
                File file = new File(fileLoc);
                if (file.exists()) {
                    try {
                        return WorkbookFactory.create(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.warn("Data note loaded!!! Workbook file for data loader service not found for location :" + fileLoc);
                }
            }
        }

        return null;
    }

    @Override
    public Map<String, JSONObject> getJSONs() {
        return null;
    }

    @Override
    public void saveDataLoaderConfigs(List<DataLoaderConfig> configs) {
        logger.debug("Saving data loader configs to the database...");
        List<DataLoaderConfig> dataLoaderConfigs = getDataLoaderConfigs();
        configRepository.saveAll(dataLoaderConfigs);
    }

    public void createUser() {

        logger.debug("Creating default admin user....");

        User user = SparrowUtil.addAuditInfo(new User(), "admin");
        user.setEmail("hillangat@gmail.com");
        user.setUserName("admin");
        user.setPassword("hlangat.ten.245.34");
        user.setFirstName("Hillary");
        user.setLastName("Langat");
        user.setNickName("Kip");
        UserRepository repository = SparrowBeanContext.getBean(UserRepository.class);
        if (repository != null) {
            repository.deleteAll();
            repository.save(user);
        }
    }

    @Override
    public List<EmailTemplate> loadEmailTemplates(List<MediaObj> mediaObjs) {
        List<EmailTemplate> emailTemplates = EmailTemplates.createTemplates(mediaObjs);
        for (EmailTemplate e : emailTemplates) {
            emailAttachmentRepo.saveAll(e.getAttachments());
            emailTemplateRepository.save(e);
        }
        return emailTemplates;
    }

    @Override
    public List<EmailReceiver> loadEmailReceivers() {
        User users = userService.getUserById(userService.getMaxUserId());
        List<Long> userIds = new ArrayList<>();
        userIds.add(users.getUserId());
        List<EmailReceiver> emailReceivers = EmailDataService.createEmailReceivers(userIds, 1L);
        emailReceiverRepo.saveAll(emailReceivers);
        return emailReceivers;
    }

    @Override
    public List<MediaObj> loadMediaObjects() {
        List<MediaObj> objs = MediaObjects.createMediaObjects();
        mediaObjRepo.saveAll(objs);
        return objs;
    }
}
