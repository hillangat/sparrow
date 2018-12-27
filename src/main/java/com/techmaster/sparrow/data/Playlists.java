package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.misc.Rating;
import com.techmaster.sparrow.entities.playlist.Playlist;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.entities.playlist.SongOrder;
import com.techmaster.sparrow.enums.RatingType;
import com.techmaster.sparrow.util.SparrowUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Playlists {

    public static List<Song> createSongs() {

        List<Song> songs = new ArrayList<>();

        for( int i = 0; i < 50; i++ ) {
            Song teBorte = SparrowUtil.addAuditInfo(new Song(), "admin");
            teBorte.setGenre("Reggaeton");
            teBorte.setName("Te Boté " + i);
            teBorte.setPicture(null);
            teBorte.setRating(0);
            teBorte.setRatingCount(0);
            teBorte.setArtist("Nio Garcia, Darell, Casper Magico " + i);
            teBorte.setProducer("Drake");
            teBorte.setReleaseDate(LocalDateTime.of(2017, 1, 1, 0, 0));

            songs.add(teBorte);

            Song kiki = SparrowUtil.addAuditInfo(new Song(), "admin");
            kiki.setName("Drake " + i);
            kiki.setGenre("Hip-Hop/Rap");
            kiki.setPicture(null);
            kiki.setRating(0);
            kiki.setRatingCount(0);
            kiki.setArtist("Nio Garcia, Darell, Casper Magico " + i);
            kiki.setProducer("Young Martino");
            kiki.setReleaseDate(LocalDateTime.of(2018, 1, 1, 0, 0));
            kiki.setAlbum("Scorpion");

            songs.add(kiki);
        }


        return songs;

    }

    public static List<Rating> createRatings(long userId) {

        List<Rating> ratings = new ArrayList<>();

        Rating rating = SparrowUtil.addAuditInfo(new Rating(), "admin");
        rating.setRating(3);
        rating.setUserId(userId);
        rating.setRatingType(RatingType.PLAYLIST);

        ratings.add(rating);

        return ratings;
    }

    public static List<SongOrder> createSongOrders( long userId, List<Song> songs ) {
        List<SongOrder> songOrders = new ArrayList<>();
        for (Song s : songs) {
            SongOrder songOrder = SparrowUtil.addAuditInfo(new SongOrder(), "admin");
            songOrder.setSong(s);
            Integer index = Double.valueOf(Math.random() * songs.size()).intValue();
            songOrder.setSongIndex(index);
            songOrder.setUserId(userId);
            index++;
            songOrders.add(songOrder);
        }
        return songOrders;
    }

    public static List<Playlist> createPlaylist(long userId, List<Song> songs) {

        List<Playlist> playlists = new ArrayList<>();

        for( int i = 0; i < 50; i++ ) {
            Playlist playlist = SparrowUtil.addAuditInfo(new Playlist(), "admin");
            playlist.setActive(true);
            playlist.setStartTime(LocalDateTime.now());
            playlist.setEndTime(LocalDateTime.now().plusHours(i));
            playlist.setPlaylistName("Tally Ho Saturday Night");
            playlist.setRatings(createRatings(userId));
            playlist.setSongs(songs);
            playlist.setSongOrders(createSongOrders(userId, songs));

            Playlist playlist2 = SparrowUtil.addAuditInfo(new Playlist(), "admin");
            playlist2.setActive(true);
            playlist2.setStartTime(LocalDateTime.now());
            playlist2.setEndTime(LocalDateTime.now().plusHours(i+1));
            playlist2.setPlaylistName("Birch Saturday Night");
            playlist2.setRatings(createRatings(userId));
            playlist2.setSongs(songs);
            playlist2.setSongOrders(createSongOrders(userId, songs));

            playlists.add(playlist);
            playlists.add(playlist2);
        }

        return playlists;
    }

}
