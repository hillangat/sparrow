package com.techmaster.sparrow.entities.playlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.entities.misc.Rating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLYLST")
public class Playlist extends AuditInfoBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLYLST_ID", nullable = false)
    private long playListId;

    @Column(name = "PLYLST_NAME", nullable = false)
    private String playlistName;

    @Column(name = "STRT_TIME", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    private LocalDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SparrowConstants.DATE_FORMAT_STRING)
    private LocalDateTime endTime;

    @Column(name = "ACTV", nullable = false)
    private boolean active = true;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="PLYLST_RTNGS",
            joinColumns=@JoinColumn(name="PLYLST_ID", referencedColumnName="PLYLST_ID"),
            inverseJoinColumns=@JoinColumn(name="RTNG_ID", referencedColumnName="RTNG_ID"))
    private List<Rating> ratings = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name="PLYLST_SNGS",
            joinColumns=@JoinColumn(name="PLYLST_ID", referencedColumnName="PLYLST_ID"),
            inverseJoinColumns=@JoinColumn(name="SNG_ID", referencedColumnName="SNG_ID"))
    private List<Song> songs = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="PLYLST_SNG_ORDRS",
            joinColumns=@JoinColumn(name="PLYLST_ID", referencedColumnName="PLYLST_ID"),
            inverseJoinColumns=@JoinColumn(name="SNG_ORDR_ID", referencedColumnName="SNG_ORDR_ID"))
    private List<SongOrder> songOrders = new ArrayList<>();


}
