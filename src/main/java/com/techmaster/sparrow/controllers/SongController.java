package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.playlist.Song;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.imports.extraction.ExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractorFactory;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.services.SongService;
import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
public class SongController extends BaseController {

    @Autowired private SongService songService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("song/{songId}")
    public ResponseEntity<ResponseData> getSongs(@PathVariable(value = "songId", required = false) Long songId) {

        Object data = songId != null && songId > 0
                ? songService.getSongById(songId) : songService.getAllSongs();
        return getResponse(true, data, null);
    }

    @DeleteMapping("song/{songId}")
    public ResponseEntity<ResponseData> deleteSong(@PathVariable(value = "songId") Long songId) {
        RuleResultBean resultBean = songService.deleteSong(songId, getUserName());
        return getResponse(true, null, resultBean);
    }

    @DeleteMapping("song/{songId}/{toStatus}")
    public ResponseEntity<ResponseData> changeSongStatus(@PathVariable("songId") Long songId, @PathVariable("toStatus") String toStatus) {
        RuleResultBean resultBean = songService.changeStatus(songId, toStatus);
        return getResponse(true, null, resultBean);
    }

    @GetMapping("song")
    public ResponseEntity<ResponseData> createOrUpdateSong(@RequestBody Song song) {
        RuleResultBean resultBean = songService.createOrUpdateSong(song, getUserName());
        return getResponse(true, song, resultBean);
    }

    @PostMapping("song/search")
    public ResponseEntity<ResponseData> searchSongs(@RequestBody GridDataQueryReq queryReq) {
        List<Song> songs = songService.searchSongs(queryReq);
        return getResponse(true, songs, null);
    }

    @PostMapping(value = "song/import")
    public ResponseEntity<ResponseData> importSongs(MultipartHttpServletRequest request ){

        logger.debug("Beginning songs import process...");
        Object[] wbkExtracts = SparrowUtil.getWorkbookFromMultiPartRequest(request);
        Workbook workbook = (Workbook)wbkExtracts[0];
        String fileName = (String)wbkExtracts[1];
        String userName = getUserName();

        ExcelExtractor locationExtractor = ExcelExtractorFactory.getIntance()
                .getExtractor(ExcelExtractor.LOCATION_EXTRACTOR, workbook, userName, fileName);

        locationExtractor.execute();

        Status status = locationExtractor.success() ? Status.SUCCESS : Status.FAILED;

        RuleResultBean resultBean = new RuleResultBean();

        if (!status.equals(Status.SUCCESS)) {
            resultBean.setError(SparrowConstants.APPLICATION_ERROR_KEY, "Import operation failed. Please check email for details.");
        }

        return getResponse(false, null, resultBean);

    }

}
