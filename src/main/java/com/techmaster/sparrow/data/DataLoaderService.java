package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.misc.DataLoaderConfig;
import com.techmaster.sparrow.entities.misc.Event;
import com.techmaster.sparrow.entities.misc.MediaObj;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Set;

public interface DataLoaderService {

    void execute();
    List<DataLoaderConfig> getDataLoaderConfigs();
    Workbook getWorkBook (DataLoaderConfig config);
    void saveDataLoaderConfigs(List<DataLoaderConfig> configs);
    List<EmailTemplate> loadEmailTemplates(List<MediaObj> mediaObjs);
    List<EmailReceiver> loadEmailReceivers();
    List<MediaObj> loadMediaObjects();
    Set<UserRole> loadUserRoles();
    List<Song> loadSongs();
    List<Playlist> loadPlaylists();
    List<Event> loadEvents(List<Playlist> playlists);
    EmailContent loadEmailContent();


}
