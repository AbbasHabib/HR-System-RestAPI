package com.spring.Security;

import com.spring.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String>
{
}
