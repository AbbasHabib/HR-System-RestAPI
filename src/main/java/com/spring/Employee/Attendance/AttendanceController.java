package com.spring.Employee.Attendance;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
}
