package com.spring.TestsByEmployee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Security.UserCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeSecurityTests extends IntegrationTest {

    @Test
    @DatabaseSetup("/TwoEmployeeWithCredentials.xml")
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

    @Test
    @DatabaseSetup("/TwoEmployeeWithCredentials.xml")
    public void get_employee_with_id_by_hr_done_by_employee_results_forbidden() throws Exception {
        long searchForId = 20L;

        getMockMvc().perform(MockMvcRequestBuilders.get("/employee/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_different_department_from_logged_employee_current_department_results_forbidden() throws Exception {
        long searchForId = 101L;

        getMockMvc().perform(MockMvcRequestBuilders.get("/department/" + searchForId)
                .with(httpBasic("lamona_habib_105", "1234")))
                .andExpect(status().isForbidden());
    }


}
