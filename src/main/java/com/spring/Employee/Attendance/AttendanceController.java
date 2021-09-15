package com.spring.Employee.Attendance;

import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.monthDetails.MonthDTO;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @PostMapping(value = "/day/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DayDetails addNewDayData(@PathVariable String id, @RequestBody DayDetails dayDetails) throws CustomException {
        return attendanceService.addNewDayDataAndSave(Long.parseLong(id), dayDetails);
    }

    @GetMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceTable getAttendanceTableByEmployeeId(@PathVariable String id) throws CustomException {
        return attendanceService.getAttendanceTableByEmployeeId(Long.parseLong(id));
    }

    @GetMapping(value = "/month/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonthDTO getMonthData(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.getMonthDetailsDTO(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value = "/absence/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer absenceDaysInYearTillMonth(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.calcAbsenceDaysInYearTillMonth(Long.parseLong(id), LocalDate.parse(date));
    }


    @GetMapping(value = "/salary/employee/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryDTO getEmployeeSalaryAtMonth(@PathVariable String id, @PathVariable String date) throws CustomException {
        return attendanceService.employeeSalaryAtMonth(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value = "/salary/all-history/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonthDetails> getSalaryHistory(@PathVariable String id) throws CustomException {
        return attendanceService.getAllSalaryHistory(Long.parseLong(id));
    }

}
