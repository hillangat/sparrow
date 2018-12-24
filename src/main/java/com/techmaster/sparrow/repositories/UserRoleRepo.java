package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserRoleRepo extends CrudRepository<UserRole, Long> {

}
