package com.techmaster.sparrow.validation;

import com.techmaster.sparrow.entities.misc.UserRole;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.enums.UserRoleType;
import com.techmaster.sparrow.repositories.SongRepo;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.UserRepo;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.services.SongService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.h2.bnf.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SongValidator extends AbstractValidator{

    public RuleResultBean validateCreateOrUpdate(Song song, String userName) {

        RuleResultBean resultBean = new RuleResultBean();
        UserRepo userRepo = SparrowBeanContext.getBean(UserRepo.class);
        Set<UserRole> userRoles = userRepo.getUserRoles(userName);

        validateEmpty(resultBean, song.getName(), "name", "Song name is required");
        validateEmpty(resultBean, song.getArtist(), "artist", "Song artist is required");
        validateEmpty(resultBean, song.getProducer(), "producer", "Song producer is required");

        if ( !(SparrowUtil.isAdmin(userRoles) || SparrowUtil.isSongManager( userRoles )) ) {
            resultBean.setError("userRole", "Admin or Song Manager roles are needed for this action");
        }

        return resultBean;
    }

    public RuleResultBean validateDelete( Song song, String userName) {

        RuleResultBean resultBean = new RuleResultBean();
        SongService songService = SparrowBeanContext.getBean(SongService.class);
        UserRepo userRepo = SparrowBeanContext.getBean(UserRepo.class);

        Set<UserRole> userRoles = userRepo.getUserRoles(userName);

        if (!song.getLifeStatus().equals(Status.DRAFT)) {
            resultBean.setError("lifeStatus", "Song can only be deleted in draft status");
        }

        if ( !(SparrowUtil.isAdmin(userRoles) || SparrowUtil.isSongManager( userRoles )) ) {
            resultBean.setError("userRole", "Admin or Song Manager roles are needed for this action");
            return resultBean;
        }

        int count = songService.getSongPlaylistCount(song.getSongId());

        if (count > 0) {
            resultBean.setError("playlist", "Song is part of " + count + " playlists");
        }

        return resultBean;
    }

    public RuleResultBean validateStatusChange( Song song, Status toStatus ) {
        RuleResultBean resultBean = new RuleResultBean();
        return resultBean;
    }

}
