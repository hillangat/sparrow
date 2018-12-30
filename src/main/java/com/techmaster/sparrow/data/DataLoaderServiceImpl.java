package com.techmaster.sparrow.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.email.EmailService;
import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.*;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.enums.*;
import com.techmaster.sparrow.imports.extraction.ExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractorFactory;
import com.techmaster.sparrow.repositories.*;
import com.techmaster.sparrow.services.apis.UserService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoaderServiceImpl implements DataLoaderService {

    @Autowired private DataLoaderConfigRepository configRepository;
    @Autowired private EmailTemplateRepo emailTemplateRepo;
    @Autowired private UserService userService;
    @Autowired private EmailReceiverRepo emailReceiverRepo;
    @Autowired private EmailAttachmentRepo emailAttachmentRepo;
    @Autowired private MediaObjRepo mediaObjRepo;
    @Autowired private SongRepo songRepo;
    @Autowired private EventRepo eventRepo;
    @Autowired private EmailContentRepo emailContentRepo;
    @Autowired private EmailService emailService;
    @Autowired private UserRoleRepo userRoleRepo;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

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
                ExcelExtractor excelExtractor = ExcelExtractorFactory.getIntance()
                        .getExtractor(c.getExtractor(),workbook, adminUserName, originalFileName);

                excelExtractor.execute();
            }
        });

        Set<UserRole> userRoles = loadUserRoles();
        createUser(userRoles);
        List<MediaObj> mediaObjs = loadMediaObjects();
        loadEmailTemplates(mediaObjs);
        loadEmailReceivers();
        loadSongs();
        List<Playlist> playlists = loadPlaylists();
        loadEvents(playlists);

        EmailContent emailContent = loadEmailContent();
        emailService.send(emailContent);
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
            FileType fileType = config.getFileType();
            if (FileType.EXCEL.equals(fileType)) {
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
    public void saveDataLoaderConfigs(List<DataLoaderConfig> configs) {
        logger.debug("Saving data loader configs to the database...");
        List<DataLoaderConfig> dataLoaderConfigs = getDataLoaderConfigs();
        configRepository.saveAll(dataLoaderConfigs);
    }

    public void createUser(Set<UserRole> roles) {

        logger.debug("Creating default admin user....");

        for( int i = 0; i < 100; i++ ) {
            User user = SparrowUtil.addAuditInfo(new User(), "admin");
            user.setEmail("hillangat" + i + "@gmail.com");
            user.setUserName("admin" + ( i == 0 ? "" : i) );

            String encodedPassword = bCryptPasswordEncoder.encode("hlangat.ten.245.34");

            user.setPassword(encodedPassword);
            user.setFirstName("Hillary");
            user.setLastName("Langat");
            user.setEmailConfirmed(false);
            user.setNickName("Kip");
            user.setUserRoles(roles);
            UserRepo repository = SparrowBeanContext.getBean(UserRepo.class);
            repository.save(user);
        }

        logger.debug("Successfully created default admin user!");
    }

    @Override
    public List<EmailTemplate> loadEmailTemplates(List<MediaObj> mediaObjs) {
        logger.debug("Loading email templates...");
        List<EmailTemplate> emailTemplates = EmailTemplates.createTemplates(mediaObjs);
        for (EmailTemplate e : emailTemplates) {
            emailAttachmentRepo.saveAll(e.getAttachments());
            emailTemplateRepo.save(e);
        }
        logger.debug("successfully loaded email templates!!!");
        return emailTemplates;
    }

    @Override
    public List<EmailReceiver> loadEmailReceivers() {
        logger.debug("Loading email receivers...");
        User users = userService.getUserById(userService.getMaxUserId());
        List<Long> userIds = new ArrayList<>();
        userIds.add(users.getUserId());
        List<EmailReceiver> emailReceivers = EmailDataService.createEmailReceivers(userIds, 1L);
        emailReceiverRepo.saveAll(emailReceivers);
        logger.debug("successfully loaded email receivers!!!");
        return emailReceivers;
    }

    @Override
    public List<MediaObj> loadMediaObjects() {
        logger.debug("Loading media objects...");
        List<MediaObj> objs = MediaObjects.createMediaObjects();
        mediaObjRepo.saveAll(objs);
        logger.debug("successfully loaded media objects!!!");
        return objs;
    }

    @Override
    public List<Song> loadSongs() {
        logger.debug("Loading songs...");
        List<Song> songs = Playlists.createSongs();
        songRepo.saveAll(songs);
        logger.debug("successfully loaded songs!!!");
        return songs;
    }

    @Override
    public List<Playlist> loadPlaylists() {
        logger.debug("Loading playlists...");
        long userId = userService.getMaxUserId();
        List<Song> songs = loadSongs();
        List<Playlist>  playlists = Playlists.createPlaylist(userId, songs);
        logger.debug("Successfully loaded playlists!!!");
        return playlists;
    }

    @Override
    public List<Event> loadEvents(List<Playlist> playlists) {
        logger.debug("Loading events for testing....");
        List<User> users = userService.getAllUsers();
        List<Location> locations = SparrowCacheUtil.getInstance().getLocationHierarchies();
        List<Rating> ratings = createRatings(100, RatingType.EVENT, users.get(0).getUserId());
        logger.debug("Successfully loaded events for testing....");
        List<Event> events = Events.loadEvents(users, playlists, locations.get(0), ratings);
        eventRepo.saveAll(events);
        return events;
    }

    private List<Rating> createRatings(int size, RatingType type, long userId) {
        List<Rating> ratings = new ArrayList<>();
        for( int i = 0; i < size; i++ ) {
            Rating rating = SparrowUtil.addAuditInfo(new Rating(), "admin");
            rating.setRatingType(type);
            Double random = Math.random() * 5;
            rating.setRating(random.intValue());
            rating.setUserId(userId);
            ratings.add(rating);
        }
        return ratings;
    }

    @Override
    public EmailContent loadEmailContent() {

        logger.debug("Loading email contents for testing...");

        List<User> users = userService.getAllUsers();

        List<EmailReceiver> receivers = users.stream().map(u -> {
            EmailReceiver receiver = SparrowUtil.addAuditInfo(new EmailReceiver(), "admin");
            receiver.setReceiverType(EmailReceiverType.TO);
            receiver.setMiddleName(u.getMiddleName());
            receiver.setLastName(u.getLastName());
            receiver.setEmail(u.getEmail());
            receiver.setFirstName(u.getFirstName());
            return receiver;
        }).collect(Collectors.toList());

        List<EmailTemplate> emailTemplates = SparrowUtil.getListOf(emailTemplateRepo.findAll());

        EmailContent emailContent = SparrowUtil.addAuditInfo(new EmailContent(), "admin");
        emailContent.setDeliveryStatus(Status.CONCEPTUAL);
        emailContent.setLifeStatus(Status.DRAFT);
        emailContent.setReasonType(EmailReasonType.ACCOUNT_CREATION_SUCCESS);
        emailContent.setReceivers(receivers);
        emailContent.setSendTime(LocalDateTime.now().plusDays(1));
        emailContent.setTemplate(emailTemplates.get(0));
        emailContent.setUser(users.get(0));
        emailContent.setSubject("Testing email content from sparrow!");

        logger.debug("Finished email content loading!!");

        return emailContentRepo.save(emailContent);
    }

    @Override
    public Set<UserRole> loadUserRoles() {
        UserRoleType[] types = UserRoleType.values();
        Set<UserRole> roles = new HashSet<>();
        Arrays.stream(types).forEach(a -> {
            UserRole userRole = SparrowUtil.addAuditInfo(new UserRole(), "admin");
            userRole.setRoleDesc(a.getDesc());
            userRole.setRoleName(a.getName());
            roles.add(userRole);
        });
        userRoleRepo.saveAll(roles);
        return roles;
    }
}
