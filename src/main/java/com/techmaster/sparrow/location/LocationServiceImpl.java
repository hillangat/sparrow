package com.techmaster.sparrow.location;

import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.repositories.LocationRepository;
import com.techmaster.sparrow.util.SparrowUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationServiceImpl implements LocationService{

    @Autowired private LocationRepository locationRepository;

    @Override
    public List<Location> getLocationHierarchies() {

        List<Location> locations = SparrowUtility.getListOf(locationRepository.findAll());

        List<Location> hierarchies = locations
                .stream()
                .filter(l -> isParent(locations, l))
                .map(p -> {

                    List<Location> subLocations = locations
                            .stream()
                            .filter(c -> c.getParentId() == p.getLocationId())
                            .collect(Collectors.toList());

                    p.setSubLocations(subLocations);

                    return p;
                }).collect(Collectors.toList());

        return hierarchies;
    }

    private boolean isParent (List<Location> locations, Location l) {
        boolean hasChildren = locations.stream()
                .anyMatch(c -> c.getParentId() == l.getLocationId());
        return hasChildren || SparrowUtility.getLongFromObject(l.getParentId()).longValue() == 0l;
    }

}
