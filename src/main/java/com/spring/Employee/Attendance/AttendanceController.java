package com.spring.Employee.Attendance;

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

    @PostMapping(value="/{id}")
    public String addNewDayDetails(@RequestBody DayDetails dayDetails, @PathVariable String id) throws CustomException
    {
        return attendanceService.addNewDayDetail(dayDetails, Long.parseLong(id));
    }

    @GetMapping(value = "/first-year/{id}")
    public DayDetails findEmployeeFirstYear(@PathVariable String id)
    {
        return attendanceService.findEmployeeFirstYear(Long.parseLong(id));
    }

}
