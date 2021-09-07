package com.spring.attendance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.dayDetails.DayDetailsRepository;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.testShortcuts.TestShortcutMethods;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.webservices.client.MockWebServiceServerTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class
,       TransactionalTestExecutionListener.class})
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
        long employeeId = 102;
        AttendanceTable expectedAttendanceTable = new AttendanceTable();
        expectedAttendanceTable.setId(102L);
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
    @Transactional
    public void addNewDayData() throws Exception, CustomException
    {
        long employeeId = 101L;
        AttendanceTable attendanceTable = attendanceService.getAttendanceTableByEmployeeId(employeeId);

        DayDetails dayToAdd = new DayDetails();
        dayToAdd.setDate(LocalDate.of(2020,10,1));
        dayToAdd.setAbsent(true);
        dayToAdd.setId(0L);
        dayToAdd.setAttendanceTable(attendanceTable);
        dayToAdd.setBonusInSalary(0.0f);
        attendanceTable.addDay(dayToAdd);

        ObjectMapper objectMapper = new ObjectMapper();
        String dayToAddDetailsJson = objectMapper.writeValueAsString(dayToAdd); // converts employee object to JSON string

        MvcResult result = mockMvc.perform(post("/attendance/"+employeeId)
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
    public void getMonthData()throws JsonProcessingException, Exception, CustomException
    {
        long employeeId = 101L;
        String month = "2020-01-01";
        AttendanceTable attendanceTable = attendanceService.getAttendanceTableByEmployeeId(employeeId);


        MonthDetails monthDetails = new MonthDetails();
        monthDetails.setId(101);
        monthDetails.setDate(LocalDate.of(2020,1,1));
        monthDetails.setAbsences(0);
        monthDetails.setBonuses(0.0f);
        monthDetails.setAttendanceTable(attendanceTable);


        ObjectMapper objectMapper = new ObjectMapper();

        String monthDetailsJson = objectMapper.writeValueAsString(monthDetails); // converts employee object to JSON string

        Map<String, String> mapExpected = objectMapper.readValue(monthDetailsJson, Map.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/attendance/month/"+employeeId+"/"+month))
                .andExpect(status().isOk())
                .andReturn();

        String resJson = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(resJson);
        String dateReceived = jsonObject.getString("date");
        Map<String, String> mapReceived = objectMapper.readValue(monthDetailsJson, Map.class);


        assertEquals(mapExpected.get("id"), mapReceived.get("id"));
        assertEquals(mapExpected.get("grossSalaryOfMonth"), mapReceived.get("grossSalaryOfMonth"));
        // for some reason date received and and expected date object differ in inner object value
        // therefore iam comparing them as strings
        assertEquals(month, dateReceived);
        assertEquals(mapExpected.get("absences"), mapReceived.get("absences"));
        assertEquals(mapExpected.get("bonuses"), mapReceived.get("bonuses"));
    }




}