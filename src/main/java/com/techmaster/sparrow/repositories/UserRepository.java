package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM USR WHERE USR_ID = ?0", nativeQuery = true)
    User findByUserId(Long userId);

    @Query(value = "SELECT u FROM User u WHERE u.userName = ?0", nativeQuery = true)
    User findByUserName(String userName);
}
