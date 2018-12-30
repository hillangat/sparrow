package com.techmaster.sparrow.services.impls;

import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.repositories.SongRepo;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.services.apis.SongService;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.SongValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired private SongService songService;
    @Autowired private SongValidator songValidator;
    @Autowired private SongRepo songRepo;

    @Override
    public RuleResultBean createOrUpdateSong(Song song, String userName) {
        RuleResultBean resultBean = songValidator.validateCreateOrUpdate(song, userName);
        if (resultBean.isSuccess()) {
            songRepo.save(song);
        }
        return resultBean;
    }

    @Override
    public RuleResultBean createOrUpdateSongs(List<Song> songs, String userName) {
        RuleResultBean resultBean = null;
        for (Song song : songs) {
            resultBean = songValidator.validateCreateOrUpdate(song, userName);
            if (!resultBean.isSuccess()) {
                break;
            }
        }
        resultBean = resultBean == null ? new RuleResultBean() : resultBean;
        if (resultBean.isSuccess()) {
            songRepo.saveAll(songs);
        }
        return resultBean;
    }

    @Override
    public Song getSongById(long songId) {
        return SparrowUtil.getIfExist(songRepo.findById(songId));
    }

    @Override
    public List<Song> searchSongs(GridDataQueryReq queryReq) {
        return null;
    }

    @Override
    public List<Song> getAllSongs() {
        return SparrowUtil.getListOf(songRepo.findAll());
    }

    @Override
    public RuleResultBean deleteSong(long songId, String userName) {
        Song song = SparrowUtil.getIfExist(songRepo.findById(songId));
        RuleResultBean resultBean = songValidator.validateDelete( song, userName );
        if (resultBean.isSuccess()) {
            songRepo.deleteById(songId);
        }
        return resultBean;
    }

    @Override
    public RuleResultBean changeStatus(long songId, String toStatus) {

        RuleResultBean resultBean = new RuleResultBean();

        if (toStatus == null || !SparrowUtil.isStatus(toStatus)) {
            resultBean.setError("status", "Value not a valid status");
            return resultBean;
        }

        Song song = SparrowUtil.getIfExist(songRepo.findById(songId));
        Status status = SparrowUtil.getStatusForStr(toStatus);
        resultBean = songValidator.validateStatusChange( song, status );

        if (resultBean.isSuccess()) {
            song.setLifeStatus(status);
            songRepo.save(song);
        }

        return resultBean;
    }

    @Override
    public int getSongPlaylistCount(long songId) {
        SparrowJDBCExecutor executor = SparrowUtil.executor();
        String query = executor.getQueryForSqlId("songPlaylistCount");
        Object countObj = executor.executeQueryForOneReturn(query, executor.getList(songId));
        Integer count = SparrowUtil.notNullNotEmpty(countObj) ? Integer.valueOf(countObj.toString()) : 0;
        return count;
    }
}
