package com.spring.attendance;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.dayDetails.DayDetailsRepository;
import com.spring.Employee.Attendance.monthDetails.MonthDTO;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.EmployeeRepository;
import com.spring.Employee.EmployeeService;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.testShortcuts.TestShortcutMethods;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class
        , TransactionalTestExecutionListener.class})
public class AttendanceIntegrationTest {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    DayDetailsRepository dayDetailsRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DatabaseSetup("/attendanceData.xml")
    public void get_attendance_table_by_hr() throws CustomException, Exception {
        long employeeId = 102;
        AttendanceTable expectedAttendanceTable = new AttendanceTable();
        expectedAttendanceTable.setId(102L);
        expectedAttendanceTable.setInitialWorkingYears(9);
        expectedAttendanceTable.setSalaryRaise(0);
        expectedAttendanceTable.setMonthDetailsList(new ArrayList<MonthDetails>());
        expectedAttendanceTable.setDailyDetailsList(new ArrayList<DayDetails>());
        expectedAttendanceTable.setEmployee(employeeService.getEmployee(employeeId));

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedAttendanceTableJson = objectMapper.writeValueAsString(expectedAttendanceTable);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();
        String res = result.getResponse().getContentAsString();
        assertEquals(expectedAttendanceTableJson, res);
    }

    @Test
    @DatabaseSetup("/attendanceData.xml")
    @Transactional
    public void add_new_day_data_by_hr() throws Exception, CustomException {
        long employeeId = 101L;
        AttendanceTable attendanceTable = attendanceService.getAttendanceTableByEmployeeId(employeeId);

        DayDetails dayToAdd = new DayDetails();
        dayToAdd.setDate(LocalDate.of(2020, 10, 1));
        dayToAdd.setAbsent(true);
        dayToAdd.setId(0L);
        dayToAdd.setAttendanceTable(attendanceTable);
        dayToAdd.setBonusInSalary(0.0f);
        attendanceTable.addDay(dayToAdd);

        ObjectMapper objectMapper = new ObjectMapper();
        String dayToAddDetailsJson = objectMapper.writeValueAsString(dayToAdd); // converts employee object to JSON string


        MvcResult result = mockMvc.perform(post("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayToAddDetailsJson))
                .andExpect(status().isOk())
                .andReturn();

        TestShortcutMethods<DayDetails> testShortcutMethods = new TestShortcutMethods<DayDetails>();
        testShortcutMethods.setObjectIdFromResponseResult(result, dayToAdd);
        testShortcutMethods.compareIdOwnerWithDataBase(dayToAdd, dayDetailsRepository);
    }


    @Test
    @DatabaseSetup("/attendanceData.xml")
    public void get_month_data_by_hr() throws Exception, CustomException {
        long employeeId = 101L;

        LocalDate monthToFind = LocalDate.of(2021,1,1);
        MonthDTO monthDTOFromDb = attendanceService.getMonthDetailsDTO(employeeId, monthToFind);

        ObjectMapper objectMapper = new ObjectMapper();
        String monthDetailsJson = objectMapper.writeValueAsString(monthDTOFromDb); // converts employee object to JSON string

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/month/employee/" + employeeId + "/" +monthToFind.toString())
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        assertEquals(monthDetailsJson, responseJson);

    }


    @Test
    @DatabaseSetup("/attendanceData.xml")
    @Transactional
    public void date_insertion_and_getting_absence_days_in_year_till_month_by_hr() throws Exception, CustomException {
        String expectedAbsence = "6";
        long employeeId = 101L;
        String month = "2020-01-01";

        AttendanceTable attendanceTable = attendanceService.getAttendanceTableByEmployeeId(employeeId);

        List<DayDetails> absenceInDaysList = new ArrayList<>();
        DayDetails bonusInDay = new DayDetails(null, attendanceTable, LocalDate.of(2020, 1, 1), false, 0.0f);

        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f));
        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f));
        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f));
        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f));
        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f));

        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, false, 0.0f)); // not absent

        absenceInDaysList.add(new DayDetails(null, attendanceTable, null, true, 0.0f)); // absent

        absenceInDaysList.get(0).setDate(LocalDate.of(2020, 1, 5));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(0));
        absenceInDaysList.get(1).setDate(LocalDate.of(2020, 1, 6));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(1));
        absenceInDaysList.get(2).setDate(LocalDate.of(2020, 1, 7));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(2));
        absenceInDaysList.get(3).setDate(LocalDate.of(2020, 1, 8));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(3));
        absenceInDaysList.get(4).setDate(LocalDate.of(2020, 1, 9));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(4));
        absenceInDaysList.get(5).setDate(LocalDate.of(2020, 1, 10));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(5));
        absenceInDaysList.get(6).setDate(LocalDate.of(2020, 1, 11));
        attendanceService.addNewDayDataAndSave(employeeId, absenceInDaysList.get(6));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/absence/employee/" + employeeId + "/" + month)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        String resJson = result.getResponse().getContentAsString();

        assertEquals(resJson, expectedAbsence);
    }


}