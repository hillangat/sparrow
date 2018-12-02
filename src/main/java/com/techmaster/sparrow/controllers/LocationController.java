package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.location.LocationService;
import com.techmaster.sparrow.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/api/location")
public class LocationController extends BaseController {

    @Autowired private LocationRepository locationRepository;
    @Autowired private LocationService locationService;

    @RequestMapping("/create")
    public ResponseEntity<ResponseData> createLocation() {
        Location location = addAuditInfo(new Location());
        location.setName("Hoboken");
        location.setLatitude(1.236);
        location.setLatitude(1.22569);

        List<Location> subLocations = new ArrayList<>();

        for( int i = 0; i < 10; i++ ) {
            Location sub = addAuditInfo(new Location());
            sub.setName("Tally Ho" + i);
            sub.setLatitude(1.236);
            sub.setLongitude(1.22569);

            List<Location> subLocations2 = new ArrayList<>();

            for( int j = 0; j < 10; j++ ) {
                Location sub2 = addAuditInfo(new Location());
                sub2.setName("Tally Ho" + j);
                sub2.setLatitude(1.236);
                sub2.setLongitude(1.22569);
                subLocations2.add(sub2);
            }

            sub.setSubLocations(subLocations2);
            subLocations.add(sub);
        }

        location.setSubLocations(subLocations);

        locationRepository.save(location);

        return ResponseEntity.ok(new ResponseData(location, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG, null));
    }

    @GetMapping(value = "/locations")
    @ResponseBody
    public ResponseEntity<ResponseData> getAllLocations() {
        List<Location> locations = SparrowCacheUtil.getInstance().getLocationHierarchies();
        return ResponseEntity.ok(new ResponseData(locations, StatusEnum.SUCCESS.getStatus(), SUCCESS_RETRIEVAL_MSG, null));
    }
}
