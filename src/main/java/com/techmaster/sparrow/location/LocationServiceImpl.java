package com.techmaster.sparrow.location;

import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.repositories.LocationRepository;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.util.SparrowUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationServiceImpl implements LocationService{

    @Autowired private LocationRepository locationRepository;
    @Autowired private SparrowJDBCExecutor sparrowJDBCExecutor;

    @Override
    public List<Location> getLocationHierarchies() {

        List<Location> locations = SparrowUtil.getListOf(locationRepository.findAll());
        locations.forEach(l -> setChildren(l, locations));
        return locations.stream().filter(location -> location.getParentId() == 0).collect(Collectors.toList());
    }

    public void setChildren(Location parent, List<Location> locations) {
        parent.setSubLocations(
                locations.stream()
                        .filter(c -> c.getParentId() == parent.getLocationId())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Location> recursivelySave(List<Location> locations) {

        // Save parents first
        locations
            .stream().filter(l -> isParent(locations, l))
            .sorted(Comparator.comparingLong(Location::getLocationId))
            .forEach(parent -> {
                parent.setLocationId(0);
                locationRepository.save(parent);
                Long newId = parent.getLocationId();
                locations.forEach(c -> {
                    if (c.getUiParentId() == parent.getUiLocationId()) {
                        c.setParentId(newId);
                    }
                });
            });

        // Save children after
        locations
            .stream().filter(l -> !isParent(locations, l))
            .sorted(Comparator.comparingLong(Location::getLocationId))
            .forEach(child -> {
                child.setLocationId(0);
                locationRepository.save(child);
            });



        return locations;
    }

    private boolean isParent (List<Location> locations, Location l) {
        String name = l.getName();
        boolean hasChildren = locations.stream()
                .anyMatch(c -> c.getUiParentId() == l.getUiLocationId());
        return hasChildren;
    }

}
