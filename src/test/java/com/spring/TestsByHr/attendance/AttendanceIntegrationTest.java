package com.spring.TestsByHr.attendance;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Employee.EmployeeLog.AttendanceTable;
import com.spring.Employee.EmployeeLog.dayDetails.DayDetails;
import com.spring.Employee.EmployeeLog.dayDetails.DayDetailsCommand;
import com.spring.Employee.EmployeeLog.dayDetails.DayDetailsDTO;
import com.spring.Employee.EmployeeLog.monthDetails.MonthDTO;
import com.spring.Employee.EmployeeLog.monthDetails.MonthDetails;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.TestsByHr.testShortcuts.TestShortcutMethods;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttendanceIntegrationTest extends IntegrationTest {

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void add_new_day_data_by_hr() throws Exception, CustomException {
        long employeeId = 101L;
        AttendanceTable attendanceTable = getAttendanceService().getAttendanceTableByEmployeeId(employeeId);

        DayDetails dayToAdd = new DayDetails();
        dayToAdd.setDate(LocalDate.of(2020, 10, 1));
        dayToAdd.setAbsent(true);
        dayToAdd.setAttendanceTable(attendanceTable);
        attendanceTable.addDay(dayToAdd);

        DayDetailsCommand dayDetailsCommand = new DayDetailsCommand();

        dayDetailsCommand.setDate("2020-10-01");
        dayDetailsCommand.setBonusInSalary(0f);
        dayDetailsCommand.setAbsent(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String dayToAddDetailsCommandJson = objectMapper.writeValueAsString(dayDetailsCommand); // converts employee object to JSON string


        MvcResult responseDTO = getMockMvc().perform(post("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayToAddDetailsCommandJson))
                .andExpect(status().isOk())
                .andReturn();

        DayDetailsDTO expectedDTOResponse = new DayDetailsDTO();
        DayDetailsDTO.setDayDetailsToDTO(dayToAdd, expectedDTOResponse);

        TestShortcutMethods<DayDetailsDTO> tester = new TestShortcutMethods<>();
        tester.setObjectIdFromResponseResult(responseDTO, expectedDTOResponse);

        assertEquals(objectMapper.writeValueAsString(expectedDTOResponse), responseDTO.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void get_attendance_with_employee_id_table_by_hr() throws CustomException, Exception {
        long employeeId = 102;
        AttendanceTable expectedAttendanceTable = new AttendanceTable();
        expectedAttendanceTable.setId(102L);
        expectedAttendanceTable.setInitialWorkingYears(9);
        expectedAttendanceTable.setMonthDetailsList(new ArrayList<MonthDetails>());
        expectedAttendanceTable.setDailyDetailsList(new ArrayList<DayDetails>());
        expectedAttendanceTable.setEmployee(getEmployeeService().getEmployee(employeeId));

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedAttendanceTableJson = objectMapper.writeValueAsString(expectedAttendanceTable);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();
        String res = result.getResponse().getContentAsString();
        assertEquals(expectedAttendanceTableJson, res);
    }


    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void get_month_data_by_hr() throws Exception, CustomException {
        long employeeId = 101L;

        LocalDate monthToFind = LocalDate.of(2021, 1, 1);
        MonthDTO monthDTOFromDb = getAttendanceService().getMonthDetailsDTO(employeeId, monthToFind);

        ObjectMapper objectMapper = new ObjectMapper();
        String monthDetailsJson = objectMapper.writeValueAsString(monthDTOFromDb); // converts employee object to JSON string

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/month/employee/" + employeeId + "/" + monthToFind)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        assertEquals(monthDetailsJson, responseJson);

    }


    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void date_insertion_and_getting_absence_days_in_year_till_month_by_hr() throws Exception, CustomException {
        String expectedAbsence = "6";
        long employeeId = 101L;
        String month = "2020-01-01";

        AttendanceTable attendanceTable = getAttendanceService().getAttendanceTableByEmployeeId(employeeId);

        List<DayDetailsCommand> absenceInDaysList = new ArrayList<>();

        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-01", true, 0.0f));
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-02", true, 0.0f));
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-03", true, 0.0f));
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-04", true, 0.0f));
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-05", true, 0.0f));
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-06", false, 0.0f)); // not absent
        absenceInDaysList.add(new DayDetailsCommand(null, "2020-01-07", true, 0.0f)); // absent

        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(0));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(1));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(2));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(3));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(4));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(5));
        getAttendanceService().addNewDayDataAndSave(employeeId, absenceInDaysList.get(6));


        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/absence/employee/" + employeeId + "/" + month)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        String resJson = result.getResponse().getContentAsString();

        assertEquals(resJson, expectedAbsence);
    }


    @Test
    @Transactional
    @DatabaseSetup(value = "/absencesTestsThroughYearTables.xml")
    public void generate_random_absences_at_year_by_hr() throws CustomException, Exception {
        int[] absencesInYearMonths = new int[12];
        long employeeId = 101L;
        String dayStr;
        String monthStr;


        String year = "2021";
        for (int month = 1; month <= 12; month++) {
            for (int dayInMonth = 1; dayInMonth <= 27; dayInMonth++) {
                if (((int) (Math.random() * 10) % 2) == 0) { // if true score absence
                    dayStr = (dayInMonth < 10) ? "0" + dayInMonth : dayInMonth + "";
                    monthStr = (month < 10) ? "0" + month : month + "";
                    String dayDate = year + "-" + monthStr + "-" + dayStr;
                    DayDetailsCommand dayDetailsToAddCommand = new DayDetailsCommand(null, dayDate, true, 0.0f);
                    getAttendanceService().addNewDayDataAndSave(employeeId, dayDetailsToAddCommand);
                    absencesInYearMonths[month - 1] += 1;
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (int monthInYear = 1; monthInYear <= 12; monthInYear++) {
            monthStr = (monthInYear < 10) ? "0" + monthInYear : monthInYear + "";
            String monthDate = year + "-" + monthStr + "-10";

            MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/month/employee/" + employeeId + "/" + monthDate)
                    .with(httpBasic("abbas_habib_1", "123")))
                    .andExpect(status().isOk())
                    .andReturn();

            String resultContent = result.getResponse().getContentAsString();
            JSONObject jsonObject = new JSONObject(resultContent);
            int absenceFromDB = jsonObject.getInt("absences");

            assertEquals(absencesInYearMonths[monthInYear - 1], absenceFromDB);
        }
    }


}