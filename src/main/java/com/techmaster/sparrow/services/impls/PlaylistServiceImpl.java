package com.techmaster.sparrow.services.impls;

import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SearchResult;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.entities.playlist.SongOrder;
import com.techmaster.sparrow.entities.playlist.SongOrderAverage;
import com.techmaster.sparrow.enums.RatingType;
import com.techmaster.sparrow.repositories.*;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.search.data.AngularDataHelper;
import com.techmaster.sparrow.services.apis.PlaylistService;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.validation.PlaylistValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired private PlaylistRepo playlistRepo;
    @Autowired private PlaylistValidator playlistValidator;
    @Autowired private RatingRepo ratingRepo;
    @Autowired private SongOrderRepo songOrderRepo;

    private Logger logger = LoggerFactory.getLogger(getClass());

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
    public RuleResultBean deletePlaylist(long playlistId, String userName) {
        RuleResultBean ruleResultBean = playlistValidator.validateDelete(playlistId, userName);
        if (ruleResultBean.isSuccess()) {
            playlistRepo.deleteById(playlistId);
        }
        return new RuleResultBean();
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
    public List<Song> contributeSongOrder(long playlistId, List<SongOrder> songOrders) {
        songOrderRepo.saveAll(songOrders);
        SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
        if (executor != null) {
            String query = executor.getQueryForSqlId("saveSongOrderToPlaylist");
            songOrders.forEach(s -> {
                executor.executeUpdate(query, executor.getList(playlistId, s.getSongOrderId()) );
            });
        }
        return getOrderedPlaylistSongs(playlistId);
    }

    @Override
    public SearchResult searchPlaylist(GridDataQueryReq queryReq) {
        return null;
    }

    @Override
    public SearchResult searchPlaylistSongs(Long playlistId, GridDataQueryReq queryReq) {

        int pageNumber = queryReq.getPageNo();
        int pageSize = queryReq.getPageSize();

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

    @Override
    public List<Song> getPlaylistSongs(long playlistId) {
        List<Song> songs = SparrowUtil.getListOf(playlistRepo.getPlaylistSongs(playlistId));
        return songs;
    }

    @Override
    public List<Song> getOrderedPlaylistSongs(long playlistId) {

        SparrowJDBCExecutor exec = SparrowUtil.executor();
        String query = exec.getQueryForSqlId("getSongOrdersForPlaylist");
        List<Map<String, Object>> rowMapList =  exec.executeQueryRowMap(query, exec.getList(playlistId));

        List<Long> songIds = rowMapList.stream()
                .map(row -> SparrowUtil.getLongFromObject(row.get("SNG_ID")))
                .collect(Collectors.toList());

        List<SongOrderAverage> orderAverages = songIds.stream().map(id -> {

            List<Map<String, Object>> songRows = rowMapList
                    .stream()
                    .filter(map -> SparrowUtil.getLongFromObject(map.get("SNG_ID")).longValue() == id)
                    .collect(Collectors.toList());

            List<Integer> indices = songRows.stream()
                    .map(map -> Integer.valueOf(map.get("SNG_INDX").toString()))
                    .collect(Collectors.toList());

            double average = indices.stream().mapToDouble(a -> a).average().getAsDouble();

            SongOrderAverage orderAverage = new SongOrderAverage();
            orderAverage.setAverage(average);
            orderAverage.setRatingCount(songRows.size());
            orderAverage.setSongId(id);
            orderAverage.setIndex( orderAverage.getAverage() / orderAverage.getRatingCount() );

            logger.debug("Order Average : " + orderAverage);

            return orderAverage;

        }).sorted((a, b) -> Double.valueOf(a.getIndex()).compareTo(b.getIndex()))
          .collect(Collectors.toList());

        List<Song> rawSongs = getPlaylistSongs(playlistId);
        List<Song> orderedSongs = new ArrayList<>();

        for (int i = 0; i < orderAverages.size(); i++) {
            SongOrderAverage ov = orderAverages.get(i);
            Song song = rawSongs.stream()
                    .filter(a -> a.getSongId() == ov.getSongId())
                    .findAny().orElse(null);
            if (song != null) {
                song.setIndex(i);
                orderedSongs.add(song);
            }
        }

        orderAverages.forEach(s -> logger.debug("Song Id:::: " + s.getSongId() + ": " + s.getIndex() + ", count -> " + s.getRatingCount()));

        return orderedSongs;
    }
}
