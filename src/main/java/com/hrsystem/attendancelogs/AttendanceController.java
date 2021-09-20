package com.hrsystem.attendancelogs;

import com.hrsystem.attendancelogs.daydetails.DayDetailsDTO;
import com.hrsystem.attendancelogs.monthdetails.MonthDTO;
import com.hrsystem.attendancelogs.monthdetails.MonthDetails;
import com.hrsystem.employee.dtos.EmployeeSalaryDTO;
import com.hrsystem.attendancelogs.daydetails.DayDetailsCommand;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @PostMapping(value = "/attendance/day/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DayDetailsDTO addNewDayData(@PathVariable String id, @RequestBody DayDetailsCommand dayDetailsCommand) throws CustomException {
        return attendanceService.addNewDayDataOrModifyAndSave(Long.parseLong(id), dayDetailsCommand, false);
    }

    @PutMapping(value = "/attendance/day/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DayDetailsDTO modifyExistedDay(@PathVariable String id, @RequestBody DayDetailsCommand dayDetailsCommand) throws CustomException {
        return attendanceService.addNewDayDataOrModifyAndSave(Long.parseLong(id), dayDetailsCommand, true);
    }

    @GetMapping(value = "/attendance/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceTable getAttendanceTableByEmployeeId(@PathVariable String id) throws CustomException {
        return attendanceService.getAttendanceTableByEmployeeId(Long.parseLong(id));
    }

    @GetMapping(value = "/attendance/month/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonthDTO getMonthData(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.getMonthDetailsDTO(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value = "/attendance/absence/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer absenceDaysInYearTillMonth(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.calcAbsenceDaysInYearTillMonth(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value = "/attendance/salary/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryDTO getEmployeeSalaryAtMonth(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.employeeSalaryAtMonth(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value = "/attendance/salary/all-history/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonthDetails> getSalaryHistory(@PathVariable String id) throws CustomException {
        return attendanceService.getAllSalaryHistory(Long.parseLong(id));
    }
    // -------------------------------- apis for employee with employee role ------------------------------------//

    @GetMapping(value = "profile/attendance/month/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonthDTO getMonthDataByLoggedUser(@PathVariable String date) throws CustomException {
        return attendanceService.getMonthDetailsByLoggedUser(LocalDate.parse(date));
    }

    @GetMapping(value = "profile/salary/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryDTO getEmployeeSalaryAtMonthByLoggedUser(@PathVariable String date) throws CustomException {
        return attendanceService.employeeSalaryAtMonthByLoggedUser(LocalDate.parse(date));
    }

    @GetMapping(value = "profile/salary/all-history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonthDetails> getSalaryHistoryByLoggedUser() throws CustomException {
        return attendanceService.getAllSalaryHistoryByLoggedUser();
    }

}
