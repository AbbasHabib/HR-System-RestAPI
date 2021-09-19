package com.spring.Security;

import com.spring.Employee.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {
    @Query(Queries.DELETE_FROM_USER_CREDENTIALS)
    @Modifying()
    void deleteById(String name);
}
