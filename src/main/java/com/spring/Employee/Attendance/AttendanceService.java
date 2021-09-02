package com.spring.Employee.Attendance;


import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService
{
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private DayDetailsRepository dailyAttendanceRepository;

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

    public AttendanceTable getAttendanceTableByEmployeeId(long employeeId) throws CustomException
    {
        AttendanceTable attendanceTable = attendanceRepository.findByEmployeeId(employeeId).orElse(null);
        if (attendanceTable != null)
            return attendanceTable;
        throw new CustomException("""
                *this employee id doesn't exist
                *or employee doesn't have an attendance table
                *-->check add attendance table api""");
    }

    public List<DayDetails> findAllAbsenceRecords(Long attendanceTableId)
    {
        return dailyAttendanceRepository.findAllByAttendanceTable_Id(attendanceTableId);
    }



    public String addNewDayDetail(DayDetails dayAttendanceData, Long attendanceTableId) throws CustomException
    {
        if (dailyAttendanceRepository.countAllByAttendanceTable_IdAndDate(attendanceTableId, dayAttendanceData.getDate()) > 0)
            throw new CustomException("Info at this date is already stored for this employee");
        AttendanceTable attendanceTable = getAttendanceTable(attendanceTableId);
        attendanceTable.addNewDayInfo(dayAttendanceData);
        dailyAttendanceRepository.save(dayAttendanceData);
        attendanceRepository.save(attendanceTable);
        return "Added Successfully";
    }


    // using this api we can get first year we started adding data to employee
    public DayDetails findEmployeeFirstYear(long attendanceTableId)
    {
        return dailyAttendanceRepository.findFirstByAttendanceTable_IdOrderByDate(attendanceTableId);
    }




}
