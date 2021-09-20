package com.spring.Employee.EmployeeLog.dayDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayDetailsRepository extends JpaRepository<DayDetails, Long> {
    List<DayDetails> findAllByAttendanceTable_Id(Long attendanceTable_id);

    Integer countAllByAttendanceTable_IdAndDate(Long attendanceTable_id, LocalDate date);
}
