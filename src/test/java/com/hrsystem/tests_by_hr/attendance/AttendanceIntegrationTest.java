package com.hrsystem.tests_by_hr.attendance;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.IntegrationTest;
import com.hrsystem.attendancelogs.AttendanceTable;
import com.hrsystem.attendancelogs.daydetails.DayDetails;
import com.hrsystem.attendancelogs.daydetails.DayDetailsCommand;
import com.hrsystem.attendancelogs.daydetails.DayDetailsDTO;
import com.hrsystem.attendancelogs.monthdetails.MonthDTO;
import com.hrsystem.attendancelogs.monthdetails.MonthDetails;
import com.hrsystem.employee.dtos.EmployeeSalaryDTO;
import com.hrsystem.employee.dtos.EmployeeSalaryDTOBuilder;
import com.hrsystem.tests_by_hr.testShortcuts.TestShortcutMethods;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.utilities.interfaces.constants.SalariesYearsConstants;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AttendanceIntegrationTest extends IntegrationTest {

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void add_new_day_data_by_hr() throws Exception {
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
    @DatabaseSetup("/absenceDaysWithMonths.xml")
    public void add_new_day_data_by_hr_and_it_exists_exception_thrown() throws Exception {
        long employeeId = 101L;
        AttendanceTable attendanceTable = getAttendanceService().getAttendanceTableByEmployeeId(employeeId);

        DayDetails dayToAdd = new DayDetails();
        dayToAdd.setDate(LocalDate.of(2020, 10, 1));
        dayToAdd.setAbsent(true);
        dayToAdd.setAttendanceTable(attendanceTable);
        attendanceTable.addDay(dayToAdd);

        DayDetailsCommand dayDetailsCommand = new DayDetailsCommand();

        dayDetailsCommand.setDate("2021-01-02");
        dayDetailsCommand.setBonusInSalary(0f);
        dayDetailsCommand.setAbsent(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String dayToAddDetailsCommandJson = objectMapper.writeValueAsString(dayDetailsCommand); // converts employee object to JSON string


        getMockMvc().perform(post("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayToAddDetailsCommandJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("this day already exists!\ncheck day modification api!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }



    @Test
    @DatabaseSetup("/absenceDaysWithMonths.xml")
    public void modify_existing_day_data_by_hr() throws Exception {
        long employeeId = 101L;
        LocalDate dayDate = LocalDate.of(2021, 1, 2);
        Long attendanceTableId = getAttendanceService().getAttendanceTableIdByEmployeeId(employeeId);

        DayDetailsCommand dayModificationCommand = new DayDetailsCommand();
        dayModificationCommand.setAbsent(false);
        dayModificationCommand.setDate(dayDate.toString());
        dayModificationCommand.setBonusInSalary(50000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String dayModificationCommandJSON = objectMapper.writeValueAsString(dayModificationCommand); // converts employee object to JSON string

        getMockMvc().perform(put("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayModificationCommandJSON))
                .andExpect(status().isOk());

        DayDetails dayModified = getDayDetailsRepository().findByAttendanceTable_IdAndDate(attendanceTableId, dayDate);
        Assertions.assertEquals(dayModified.isAbsent(), dayModificationCommand.isAbsent());
        Assertions.assertEquals(dayModified.getBonusInSalary(), dayModificationCommand.getBonusInSalary());
    }

    @Test
    @DatabaseSetup("/absenceDaysWithMonths.xml")
    public void modify_existing_day_without_giving_a_data_done_by_hr() throws Exception {
        long employeeId = 101L;

        DayDetailsCommand dayModificationCommand = new DayDetailsCommand();
        dayModificationCommand.setAbsent(false);
        dayModificationCommand.setBonusInSalary(50000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String dayModificationCommandJSON = objectMapper.writeValueAsString(dayModificationCommand); // converts employee object to JSON string

        getMockMvc().perform(put("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayModificationCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("dayDate cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DatabaseSetup("/absenceDaysWithMonths.xml")
    public void add_new_day_without_giving_a_data_done_by_hr() throws Exception {
        long employeeId = 101L;

        DayDetailsCommand dayModificationCommand = new DayDetailsCommand();
        dayModificationCommand.setAbsent(false);
        dayModificationCommand.setBonusInSalary(50000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String dayModificationCommandJSON = objectMapper.writeValueAsString(dayModificationCommand); // converts employee object to JSON string

        getMockMvc().perform(post("/attendance/day/employee/" + employeeId)
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(dayModificationCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("dayDate cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void get_attendance_with_employee_id_table_by_hr() throws Exception {
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
    public void get_month_data_by_hr() throws Exception {
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
    @DatabaseSetup("/employeeWithDailyData.xml")
    public void get_month_salary_by_hr() throws Exception {
        long employeeId = 101L;
        LocalDate monthToFind = LocalDate.of(2021, 1, 1);
        float grossSalary = 100000;
        int absences = 10;
        float netSalary = grossSalary;
        netSalary = netSalary * (1 - SalariesYearsConstants.TAXES) - SalariesYearsConstants.DEDUCTED_INSURANCE;

        EmployeeSalaryDTOBuilder salaryDTOBuilder = new EmployeeSalaryDTOBuilder();
        salaryDTOBuilder.setInfoDate(monthToFind)
                .setGrossSalary(grossSalary)
                .setNetSalary(netSalary)
                .setNumberOfAbsencesThroughYear(absences)
                .setNumberOfAbsencesInMonth(absences)
                .setAllowedAbsencesThroughYear(SalariesYearsConstants.AVAILABLE_ABSENCES_JUNIOR)
                .setExceededBy(0);

        EmployeeSalaryDTO employeeSalaryDTO = salaryDTOBuilder.build();

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/attendance/salary/employee/" + employeeId + "/" + monthToFind)
                .with(httpBasic("abbas_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedMonthSalaryDtoJSON = objectMapper.writeValueAsString(employeeSalaryDTO);
        String responseJson = result.getResponse().getContentAsString();
        assertEquals(expectedMonthSalaryDtoJSON, responseJson);

    }


    @Test
    @Transactional
    @DatabaseSetup("/attendanceData.xml")
    public void date_insertion_and_getting_absence_days_in_year_till_month_by_hr() throws Exception {
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

        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(0), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(1), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(2), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(3), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(4), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(5), false);
        getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, absenceInDaysList.get(6), false);


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
    public void generate_random_absences_at_year_by_hr() throws Exception {
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
                    getAttendanceService().addNewDayDataOrModifyAndSave(employeeId, dayDetailsToAddCommand, false);
                    absencesInYearMonths[month - 1] += 1;
                }
            }
        }

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