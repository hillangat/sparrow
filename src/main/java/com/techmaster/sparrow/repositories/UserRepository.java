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

    @Query(value = "UPDATE User u SET u.locked = ?0 WHERE u.userId = ?1, u.lockedBy = ?2, u.lockedReason = ?3", nativeQuery = true)
    void lockUnlockUser(String status, Long userId, long lockerUserId, String reason);

    @Query(value = "UPDATE USR u SET u.EML = ?0 WHERE u.USR_ID = ?1", nativeQuery = true)
    void changeEmail(String email, Long userId);

    @Query(value = "UPDATE User u SET u.userName = ?0 WHERE u.userId = ?1", nativeQuery = true)
    void changeUserName(String userName, Long userId);

    @Query(value = "UPDATE User u SET u.profilePic = null WHERE u.userId = ?0", nativeQuery = true)
    void deleteUserProfilePic(Long userId);

    @Query(value = "SELECT MAX(u.userId) FROM User u", nativeQuery = false)
    Long getMaxUserId();
}
