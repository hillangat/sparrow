package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.repositories.PlaylistRepo;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class PlaylistValidator extends AbstractValidator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RuleResultBean validateCreate(Playlist playlist) {
        RuleResultBean resultBean = new RuleResultBean();
        // playlist name
        validateSpecialChar( resultBean, playlist.getPlaylistName(),
                "playlistName", "Playlist cannot have special characters." );
        validateEmpty(resultBean, playlist.getPlaylistName(),
                "playlistName", "Playlist name is required");


        return resultBean;
    }

    public RuleResultBean validateDelete( long playlistId, String userName ) {
        PlaylistRepo playlistRepo = SparrowBeanContext.getBean(PlaylistRepo.class);
        UserRepo userRepo = SparrowBeanContext.getBean(UserRepo.class);
        RuleResultBean resultBean = new RuleResultBean();

        Set<UserRole> roles = userRepo.getUserRoles(userName);

        if ( SparrowUtil.isAdmin(roles) ) {
            logger.debug("User has admin rights, delete allowed with no conditions...");
            return resultBean;
        }

        if (userName == null) {
            resultBean.setError("userName", "You must be logged in to delete a playlist");
        }

        List<Object[]> times = playlistRepo.getPlaylistDeleteDetails(playlistId);
        LocalDateTime start = (LocalDateTime)times.get(0)[0];
        LocalDateTime end = (LocalDateTime)times.get(0)[1];
        String createdBy = (String)times.get(0)[2];

        if (start.isAfter(LocalDateTime.now()) && end.isBefore(LocalDateTime.now()) ) {
            resultBean.setError("startTime", "Playlist is already running");
        }

        if (createdBy != null && !createdBy.equalsIgnoreCase(userName)) {
            resultBean.setError("createdBy", "Only the creator can delete the the playlist");
        }

        return resultBean;
    }

}
