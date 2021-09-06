package com.spring.attendance;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.dayDetails.DayDetailsRepository;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.Employee;
import com.spring.testShortcuts.TestShortcutMethods;
import org.springframework.data.annotation.Transient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.spring.ExceptionsCustom.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class AttendanceIntegrationTest
{
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    DayDetailsRepository dayDetailsRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DatabaseSetup("/attendanceData.xml")
    public void get_attendance_table() throws CustomException, Exception
    {
        long employeeId = 101L;
        AttendanceTable expectedAttendanceTable = new AttendanceTable();
        expectedAttendanceTable.setId(101L);
        expectedAttendanceTable.setInitialWorkingYears(0);
        expectedAttendanceTable.setSalaryRaise(0);
        expectedAttendanceTable.setMonthDetailsList(new ArrayList<MonthDetails>());
        expectedAttendanceTable.setDailyDetailsList(new ArrayList<DayDetails>());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedAttendanceTableJson = objectMapper.writeValueAsString(expectedAttendanceTable);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/" + employeeId))
                .andExpect(status().isOk())
                .andReturn();
        String res = result.getResponse().getContentAsString();
        assertEquals(expectedAttendanceTableJson, res);
    }

    @Test
    @DatabaseSetup("/attendanceData.xml")
    public void addNewDayData() throws CustomException, Exception
    {
        long employeeId = 101L;
        AttendanceTable attendanceTable = attendanceService.getAttendanceTableByEmployeeId(employeeId);
        DayDetails dayToAdd = new DayDetails();
        dayToAdd.setDate(LocalDate.of(2020,10,1));
        dayToAdd.setAbsent(true);
        dayToAdd.setAttendanceTable(attendanceTable);
        dayToAdd.setBonusInSalary(0.0f);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/" + employeeId))
                .andExpect(status().isOk())
                .andReturn();


        TestShortcutMethods<DayDetails> testShortcutMethods = new TestShortcutMethods<DayDetails>();
        testShortcutMethods.setObjectIdFromResponseResult(result, dayToAdd);
        testShortcutMethods.compareWithDataBaseUsingId(result, dayToAdd, dayDetailsRepository);

    }


}
