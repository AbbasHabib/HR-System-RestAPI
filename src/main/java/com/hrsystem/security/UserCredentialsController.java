package com.hrsystem.security;

import com.hrsystem.attendancelogs.daydetails.DayDetailsDTO;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCredentialsController {
    @Autowired
    UserPrincipalDetailsService userPrincipalDetailsService;

    @PutMapping(value = "/security/password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public String modifyPasswordForLoggedUser(@RequestBody passwordChangeCommand newPasswordCommand) throws CustomException {
        return userPrincipalDetailsService.setNewPasswordForLoggedUser(newPasswordCommand);
    }
}
