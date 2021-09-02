package com.spring.Employee.Attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DayDetailsRepository extends JpaRepository<DayDetails, Long>
{
   // List<DailyAttendance> findAllByAttendanceTable_IdAndDate_YearAndDate_Month(Long attendanceTable_id, int date_year, short date_month);
    List<DayDetails> findAllByAttendanceTable_Employee_Id(Long attendanceTable_employee_id);
    List<DayDetails> findAllByAttendanceTable_Id(Long attendanceTable_id);
    Integer countAllByAttendanceTable_IdAndDate(Long attendanceTable_id, LocalDate date);

    DayDetails findFirstByAttendanceTable_IdOrderByDate(Long attendanceTable_id); // find first date ever for this employee in the company
}
