package com.techmaster.sparrow.services;

import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SearchArg;
import com.techmaster.sparrow.entities.misc.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.SongOrder;
import com.techmaster.sparrow.enums.RatingType;
import com.techmaster.sparrow.repositories.*;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.search.data.AngularDataHelper;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.PlaylistValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired private PlaylistRepo playlistRepo;
    @Autowired private PlaylistValidator playlistValidator;
    @Autowired private RatingRepo ratingRepo;
    @Autowired private SongOrderRepo songOrderRepo;

    @Override
    public Playlist getPlaylistById(long playlistId) {
        return SparrowUtil.getIfExist(playlistRepo.findById(playlistId));
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = SparrowUtil.getListOf(playlistRepo.findAll());
        return playlists;
    }

    @Override
    public RuleResultBean saveOrEditPlaylist(Playlist playlist) {
        RuleResultBean ruleResultBean = playlistValidator.validateCreate(playlist);
        return ruleResultBean;
    }

    @Override
    public RuleResultBean deletePlaylist(long playlistId) {
        playlistRepo.deleteById(playlistId);
        return null;
    }

    @Override
    public void ratePlaylist(long playlistIid, long userId, int rating) {
        Rating ratBean = new Rating();
        ratBean.setRating(rating);
        ratBean.setRatingType(RatingType.PLAYLIST);
        ratBean.setUserId(userId);
        ratBean.setRatingId(0);
        ratingRepo.save(ratBean);
    }

    @Override
    public Playlist contributeSongOrder(long playlistId, List<SongOrder> songOrder) {
        songOrderRepo.saveAll(songOrder);
        SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
        if (executor != null) {
            String query = executor.getQueryForSqlId("saveSongOrderToPlaylist");
            songOrder.forEach(s -> {
                executor.executeUpdate(query, executor.getList(playlistId, s.getSongOrderId()) );
            });
        }
        return null;
    }

    @Override
    public SearchResult searchPlaylist(SearchArg arg) {
        return null;
    }

    @Override
    public SearchResult searchPlaylistSongs(Long playlistId, SearchArg arg) {

        int pageNumber = arg.getPageNo();
        int pageSize = arg.getPageSize();

        EntityManager entityManager = SparrowBeanContext.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = cb
                .createQuery(Long.class);

        countQuery.select(cb
                .count(countQuery.from(Playlist.class)));

        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<Playlist> criteriaQuery = cb
                .createQuery(Playlist.class);

        Root<Playlist> from = criteriaQuery.from(Playlist.class);
        CriteriaQuery<Playlist> select = criteriaQuery.select(from);

        TypedQuery<Playlist> typedQuery = entityManager.createQuery(select);

        while (pageNumber < count.intValue()) {
            typedQuery.setFirstResult(pageNumber - 1);
            typedQuery.setMaxResults(pageSize);
            System.out.println("Current page: " + typedQuery.getResultList());
            pageNumber += pageSize;
        }

        List<Playlist> playlists = typedQuery.getResultList();
        int total = count.intValue();

        SearchResult searchResult = new SearchResult();
        searchResult.setData(playlists);
        searchResult.setPageNo(pageNumber);
        searchResult.setPageSize(pageSize);
        searchResult.setTotal(total);

        return searchResult;
    }

    @Override
    public ResponseData paginate(GridDataQueryReq queryReq) {
        ResponseData responseData = AngularDataHelper.getIntance().getBeanForQuery(Playlist.class, "playlistPagination", null);
        return responseData;
    }
}
