package com.spring.Employee.Attendance;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeRepository;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService
{
    @Autowired
    private AttendanceRepository attendanceRepository;

    public AttendanceTable findAttendanceTable(Long attendanceTableId) throws CustomException
    {
        // if no table with this id is found return null
        return attendanceRepository.findById(attendanceTableId).orElse(null);
    }

    public AttendanceTable getAttendanceTable(Long attendanceTableId) throws CustomException
    {
        AttendanceTable attendanceTableToFind = this.findAttendanceTable(attendanceTableId);
        if (attendanceTableToFind == null)
            throw new CustomException("this table id doesnt exist");
        else
            return attendanceTableToFind;
    }
}
