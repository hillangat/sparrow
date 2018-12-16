package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.imports.extraction.ExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractorFactory;
import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class ImportController extends BaseController{

    public static Logger logger = LoggerFactory.getLogger(ImportController.class);


    @PostMapping(value = "/import/location")
    public ResponseEntity<ResponseData> importReceiverRegions(MultipartHttpServletRequest request ){

        logger.debug("Beginning receiver group receivers import process...");
        Object[] wbkExtracts = SparrowUtil.getWorkbookFromMultiPartRequest(request);
        Workbook workbook = (Workbook)wbkExtracts[0];
        String fileName = (String)wbkExtracts[1];
        String userName = getUserName();

        ExcelExtractor locationExtractor = ExcelExtractorFactory.getExtractor(ExcelExtractor.LOCATION_EXTRACTOR, workbook, userName, fileName);
        locationExtractor.execute();

        Status status = locationExtractor.success() ? Status.SUCCESS : Status.FAILED;
        String message = status.equals(Status.SUCCESS) ? "Successfully completed import, please check email for details" :
                "Import operation failed. Please check email for details.";

        return ResponseEntity.ok(new ResponseData(null, status.getStatus(), message, null, 0));

    }

}
