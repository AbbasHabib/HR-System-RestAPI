package com.spring.Employee.Attendance.monthDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthDetailsRepository extends JpaRepository<MonthDetails, Long>
{
    //    DayDetails findFirstByAttendanceTable_IdOrderByDate(Long attendanceTable_id); // find first date ever for this employee in the company
    Optional<MonthDetails> findByDateAndAttendanceTable_Id(LocalDate date, Long attendanceTable_id);
    List<MonthDetails> findAllByDateBetweenAndAttendanceTable_Id(LocalDate date_from, LocalDate date_to, Long attendanceTable_id);
    List<MonthDetails> findAllByAttendanceTable_IdAndGrossSalaryOfMonthNotNull(Long attendanceTable_id);
//    List<MonthDetails> findAllByDateWithin();
    MonthDetails findFirstByAttendanceTable_IdOrderByDateAsc(Long attendanceTable_id);
    MonthDetails findFirstByAttendanceTable_IdOrderByDateDesc(Long attendanceTable_id);
}
