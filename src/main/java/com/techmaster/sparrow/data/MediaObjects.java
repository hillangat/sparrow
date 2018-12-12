package com.techmaster.sparrow.data;

import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.entities.misc.MediaObj;
import com.techmaster.sparrow.enums.StorageType;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class MediaObjects {

    private static Logger logger = LoggerFactory.getLogger(MediaObj.class);

    public static List<MediaObj> createMediaObjects() {

        String[] filesNames = new String[]{ "kenyatta_official_portrait.jpg", "company_logo.jpg" };
        List<MediaObj> objs = new ArrayList<>();

        for( String name : filesNames ) {
            String path = SparrowURLConstants.MEDIA_FILES_FOLDER + "\\" + name;
            File file = new File(path);
            if (file.exists()) {
                try {
                    MediaObj obj = SparrowUtil.addAuditInfo(new MediaObj(), "admin");
                    obj.setByteSize(file.length());

                    Blob blob = SparrowUtil.getSqlBlobForFile(file);
                    obj.setContent(blob);
                    obj.setContentType(SparrowUtil.getFileExtension(file));
                    obj.setDescription(name);
                    obj.setExtension(obj.getContentType());
                    obj.setMediaId(0);
                    obj.setOriginalName(file.getName());
                    obj.setOwner("Sparrow");
                    obj.setStorageType(StorageType.DB);
                    obj.setUrl(null);

                    objs.add(obj);

                } catch (Exception e) {

                }
            } else {
                logger.debug( "File not found in the path : " + path );
            }
        }

        return objs;
    }

}
