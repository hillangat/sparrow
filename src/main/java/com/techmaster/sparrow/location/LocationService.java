package com.techmaster.sparrow.location;

import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;

import java.util.List;

public interface LocationService {

    List<Location> getLocationHierarchies();
    List<Location> recursivelySave( List<Location> locations );
    Location getLocationById(Long locationId);
    List<Location> getLocationChildrenById(Long locationId);
    RuleResultBean save(Location location);
    void deleteLocation(Long locationId);

}
