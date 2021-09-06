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
    public AttendanceTable getAttendanceTableByEmployeeId(@PathVariable String id) throws CustomException
    {
        return attendanceService.getAttendanceTableByEmployeeId(Long.parseLong(id));
    }

    @PostMapping(value="/day/{id}") // ex adding new absence
    public DayDetails addNewDayData(@RequestBody DayDetails dayDetails, @PathVariable String id) throws CustomException
    {
        return attendanceService.addNewDayData(Long.parseLong(id), dayDetails);
    }

    @GetMapping(value ="/month/{id}/{date}")
    public MonthDetails getMonthData(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.getMonthData(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping(value ="/absence/{id}/{date}")
    public Integer AbsenceDaysInYearTillMonth(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.calcAbsenceDaysInYearTillMonth(Long.parseLong(id), LocalDate.parse(date));
    }


    @GetMapping(value = "/salary/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryDTO getEmployeeSalary(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        return attendanceService.employeeSalaryAtMonth(Long.parseLong(id), LocalDate.parse(date));
    }



}
