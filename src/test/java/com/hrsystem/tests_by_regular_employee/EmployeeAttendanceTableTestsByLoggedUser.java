package com.hrsystem.tests_by_regular_employee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.IntegrationTest;
import com.hrsystem.attendancelogs.monthdetails.MonthDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeAttendanceTableTestsByLoggedUser extends IntegrationTest {

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void get_month_data_by_regular_employee() throws Exception {
        long employeeId = 101L;

        LocalDate monthToFind = LocalDate.of(2021, 1, 1);
        MonthDTO monthDTOFromDb = getAttendanceService().getMonthDetailsDTO(employeeId, monthToFind);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedMonthDetailsJson = objectMapper.writeValueAsString(monthDTOFromDb); // converts employee object to JSON string

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/profile/attendance/month/2021-01-01")
                .with(httpBasic("big_rami_101", "1234")))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        assertEquals(expectedMonthDetailsJson, responseJson);

    }


    // duplicates and exception handling tests *********************************

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void get_month_by_different_employee_data_done_by_regular_employee_forbidden() throws Exception {
        long employeeId = 101L;

        LocalDate monthToFind = LocalDate.of(2021, 1, 1);
        getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/month/employee/" + employeeId + "/" + monthToFind)
                .with(httpBasic("big_rami_101", "1234")))
                .andExpect(status().isForbidden());
    }

}
