package com.hrsystem.security;

import com.hrsystem.employee.EmployeeRepository;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    @Autowired
    UserCredentialsRepository userCredentialsRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public UserPrincipalDetailsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }


    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = this.userCredentialsRepository.findById(username).orElse(null);
        if (userCredentials == null)
            throw new UsernameNotFoundException("userName not found!");
        return new UserPrincipal(userCredentials);
    }

    public String getLoggedUserName() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

    public String setNewPasswordForLoggedUser(passwordChangeCommand newPasswordCommand) throws CustomException {
        String loggedUserName = this.getLoggedUserName();
        UserCredentials userCredentials = this.userCredentialsRepository.findById(loggedUserName).orElse(null);
        if (userCredentials == null)
            throw new CustomException("userName cannot be found!");
        if (!BCrypt.checkpw(newPasswordCommand.getCurrentPassword(), userCredentials.getPassword()))
            throw new CustomException("current password does not match actual current password!");
        else {
            userCredentials.setPassword(passwordEncoder().encode(newPasswordCommand.getNewPassword()));
            userCredentialsRepository.save(userCredentials);
            return "password updated!";
        }
    }
}