package com.spring.TestsByEmployee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Employee.COMMANDS.EmployeeModifyCommand;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Security.UserCredentials;
import com.spring.Team.Team;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeSecurityTests extends IntegrationTest{

    @Test
    @DatabaseSetup("/TwoEmployeeWithCredentials_Hr_Employee.xml")
    public void delete_employee_with_id_with_credentials_deletion_by_hr_done_by_employee_results_forbidden() throws Exception {
        // employee trying to access HR api

        long deleteUserWithID = 20L;
        String userNameToDelete = "abbas_habib_20";
        UserCredentials userCredential = getUserCredentialsRepository().findById(userNameToDelete).orElse(null);
        Assertions.assertNotEquals(userCredential, null); // making sure that this user credential exists at first

        getMockMvc().perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(status().isForbidden());
    }
}
