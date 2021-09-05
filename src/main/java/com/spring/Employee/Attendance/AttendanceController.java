package com.spring.Employee.Attendance;

import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/attendance")
public class AttendanceController
{
    @Autowired
    AttendanceService attendanceService;

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceTable getAttendanceTable(@PathVariable String id) throws CustomException
    {
        return attendanceService.getAttendanceTable(Long.parseLong(id));
    }
    @GetMapping(value="/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AttendanceTable getAttendanceTableByEmployeeId(@PathVariable String id) throws CustomException
    {
        return attendanceService.getAttendanceTableByEmployeeId(Long.parseLong(id));
    }

    @PostMapping(value="/{id}") // ex adding new absence
    public String addNewDayInfo(@RequestBody DayDetails dayDetails, @PathVariable String id) throws CustomException
    {
        return attendanceService.addNewDayInfo(Long.parseLong(id), dayDetails);
    }

    @GetMapping(value ="/month/{id}/{date}") // {id} attendance table id
    public MonthDetails getMonthData(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.getMonthData(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value ="/absence/{id}/{date}") // {id} attendance table id
    public Integer AbsenceDaysInYearTillMonth(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.calcAbsenceDaysInYearTillMonth(Long.parseLong(id), LocalDate.parse(date));
    }


    @GetMapping(value = "/employee/salary/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)// {id} employee table id
    public EmployeeSalaryDTO getEmployeeSalary(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.employeeSalaryAtMonth(Long.parseLong(id), LocalDate.parse(date));
    }



}
