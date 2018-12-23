package com.techmaster.sparrow.location;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.entities.misc.Location;
import com.techmaster.sparrow.repositories.LocationRepository;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationServiceImpl implements LocationService {

    @Autowired private LocationRepository locationRepository;

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

        /* Save parents first */
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

        /* Save children after */
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
        boolean hasChildren = locations.stream()
                .anyMatch(c -> c.getUiParentId() == l.getUiLocationId());
        return hasChildren;
    }

    @Override
    public Location getLocationById(Long locationId) {
        if (locationId != null && locationId > 0) {
            List<Location> locations = SparrowCacheUtil.getInstance().getLocationHierarchies();
            return getLocation(locations, locationId);
        }
        return null;
    }

    private Location getLocation(List<Location> locations, long locationIId) {
        for (Location l : locations) {
            if (l.getLocationId() == locationIId) {
                return l;
            } else {
                if (SparrowUtil.isCollNotEmpty(l.getSubLocations())) {
                    l = getLocation(l.getSubLocations(), locationIId);
                    if (l != null) {
                        return l;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public RuleResultBean save(Location location) {
        locationRepository.save(location);
        SparrowCacheUtil.getInstance().refreshAllLocations();
        return new RuleResultBean();
    }

    @Override
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
        SparrowCacheUtil.getInstance().refreshAllLocations();
    }

    @Override
    public List<Location> getLocationChildrenById(Long locationId) {
        Location location = getLocationById(locationId);
        List<Location> locations = location == null ? new ArrayList<>() : location.getSubLocations();
        return locations;
    }
}
