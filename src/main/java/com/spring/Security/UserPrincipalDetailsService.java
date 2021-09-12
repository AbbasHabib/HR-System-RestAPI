package com.spring.Security;

import com.spring.Employee.EmployeeRepository;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService
{
    @Autowired
    UserCredentialsRepository userCredentialsRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public UserPrincipalDetailsService(UserCredentialsRepository userCredentialsRepository)
    {
        this.userCredentialsRepository = userCredentialsRepository;
    }


    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        UserCredentials userCredentials = this.userCredentialsRepository.getById(username);
        return new UserPrincipal(userCredentials);
    }

    public String getLoggedUserName()
    {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }
}