package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.entities.misc.Location;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.location.LocationService;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController extends BaseController {

    @Autowired private LocationService locationService;

    @PostMapping(value = "location")
    public ResponseEntity<ResponseData> createLocation(@RequestBody Location location) {
        RuleResultBean resultBean = locationService.save(location);
        return getResponse(false, location, resultBean);
    }

    @GetMapping(value = "location")
    public ResponseEntity<ResponseData> getLocations() {
        List<Location> locations = SparrowCacheUtil.getInstance().getLocationHierarchies();
        return getResponse(true, locations, new RuleResultBean());
    }

    @GetMapping(value = "location/{locationId}")
    public ResponseEntity<ResponseData> getLocation(@PathVariable(value = "locationId", required = false) Long locationId) {
        Location location = locationService.getLocationById(locationId);
        return getResponse(true, location, new RuleResultBean());
    }

    @GetMapping(value = "location/{locationId}/children")
    public ResponseEntity<ResponseData> getLocationChildren(@PathVariable(value = "locationId", required = false) Long locationId) {
        List<Location> children = locationService.getLocationChildrenById(locationId);
        return getResponse(true, children, new RuleResultBean());
    }

    @DeleteMapping(value = "location/{locationId}")
    public ResponseEntity<ResponseData> deleteLocation(@PathVariable(value = "locationId", required = true) Long locationId) {
        locationService.deleteLocation(locationId);
        return getResponse(false, null, new RuleResultBean());
    }
}
