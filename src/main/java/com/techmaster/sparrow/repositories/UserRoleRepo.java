package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.misc.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleRepo extends CrudRepository<UserRole, Long> {

    @Query("SELECT r FROM UserRole r WHERE r.roleName = ?1")
    List<UserRole> findByRoleName( String roleName );

}
