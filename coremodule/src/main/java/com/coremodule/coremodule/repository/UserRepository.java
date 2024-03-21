package com.coremodule.coremodule.repository;

import com.coremodule.coremodule.entities.users.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    List<User> findUserByemailId(String email);

    @Query("UPDATE User u SET u.failedAttempt = :failAttempts WHERE u.emailId = :email")
    @Modifying
    public void updateFailedAttempts(@Param("failAttempts") Integer failAttempts,@Param("email") String email);
}
