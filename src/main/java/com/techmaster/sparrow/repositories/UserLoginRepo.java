package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.misc.UserLogin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserLoginRepo extends CrudRepository<UserLogin, Long> {

    @Query(value="SELECT * FROM USR_LGN WHERE USR_NAM ?1 AND STS = ?2 ORDER BY CRET_DT DESC LIMIT 0, ?3", nativeQuery=true)
    List<UserLogin> getLatestLogins(String userName, String status, int count);
}
