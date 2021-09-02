package com.spring.Employee.Attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MonthDetailsRepository extends JpaRepository<MonthDetails, Long>
{
    //    DayDetails findFirstByAttendanceTable_IdOrderByDate(Long attendanceTable_id); // find first date ever for this employee in the company
    Optional<MonthDetails> findByDateAndAttendanceTable_Id(LocalDate date, Long attendanceTable_id);
}
