package com.spring.Employee.Attendance;


import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService
{
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private DayDetailsRepository dailyAttendanceRepository;

    @Autowired
    private MonthDetailsRepository monthDetailsRepository;

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

    // this api return a list of all days info where employee had stored day info
    public List<DayDetails> findAllStoredDaysInfo(Long attendanceTableId)
    {
        return dailyAttendanceRepository.findAllByAttendanceTable_Id(attendanceTableId);
    }


    public String addNewDayInfo(DayDetails dayDetails, Long attendanceTableId) throws CustomException
    {
        if (dailyAttendanceRepository.countAllByAttendanceTable_IdAndDate(attendanceTableId, dayDetails.getDate()) > 0)
            throw new CustomException("Info at this date is already stored for this employee");

        // get attendance table to add month and day data into
        AttendanceTable attendanceTable = getAttendanceTable(attendanceTableId);

        // get month this function will create it not found
        MonthDetails monthOfThatDay = getMonthOfDayAndCreateIfNotFound(dayDetails.getDate(), attendanceTable.getId());

        // inject month and day inside the attendance table
        injectDayAndMonthToAttendanceTable(dayDetails, monthOfThatDay, attendanceTable);

        // calculate and insert absences and bounces at this month
        insertDayDataToMonth(dayDetails, monthOfThatDay);


        // save month, day and attendance table into database
        dailyAttendanceRepository.save(dayDetails);
        monthDetailsRepository.save(monthOfThatDay);
        attendanceRepository.save(attendanceTable);
        return "Added Successfully";
    }

    public void injectDayAndMonthToAttendanceTable(DayDetails dayDetails, MonthDetails monthOfThatDay, AttendanceTable attendanceTable)
    {
        // inject to attendance table dayDetails
        dayDetails.setAttendanceTable(attendanceTable);
        if (monthOfThatDay.getAttendanceTable() == null)
        {
            // if month didn't exist before add new Day and Month to attendance Table lists
            monthOfThatDay.setAttendanceTable(attendanceTable);
            attendanceTable.addMonthAndDayDetails(dayDetails, monthOfThatDay);
        }
        else
            // if month existed just add the day to attendanceTable-> Days List
            attendanceTable.addDay(dayDetails);

    }

    private void insertDayDataToMonth(DayDetails dayDetails, MonthDetails monthOfThatDay)
    {
        if (dayDetails.isAbsent())
            monthOfThatDay.absences += 1;
        if (dayDetails.getBonusInSalary() > 0)
            monthOfThatDay.bonuses += dayDetails.getBonusInSalary();
    }


    public MonthDetails getMonthOfDayAndCreateIfNotFound(LocalDate date, Long attendanceTableId)
    {
        // 1 doesn't mean any thing all I care about is month and year
        LocalDate monthDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        MonthDetails monthToFind = monthDetailsRepository.findByDateAndAttendanceTable_Id(monthDate, attendanceTableId).orElse(null);
        if (monthToFind == null)
        {
            monthToFind = new MonthDetails(monthDate);
        }
        return monthToFind;
    }

    public DayDetails findEmployeeFirstYear(long attendanceTableId)
    {
        return dailyAttendanceRepository.findFirstByAttendanceTable_IdOrderByDate(attendanceTableId);
    }

    public MonthDetails getMonthData(long AttendanceTableId, LocalDate date)
    {
        return monthDetailsRepository.findByDateAndAttendanceTable_Id(date, AttendanceTableId).orElse(null);
    }
}
