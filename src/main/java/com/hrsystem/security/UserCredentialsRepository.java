package com.hrsystem.security;

import com.hrsystem.utilities.interfaces.constants.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {
    @Query(Queries.DELETE_FROM_USER_CREDENTIALS)
    @Modifying()
    void deleteById(String name);
}
