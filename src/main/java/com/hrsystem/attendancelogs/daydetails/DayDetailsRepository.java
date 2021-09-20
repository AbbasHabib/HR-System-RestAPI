package com.hrsystem.attendancelogs.daydetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DayDetailsRepository extends JpaRepository<DayDetails, Long> {
    List<DayDetails> findAllByAttendanceTable_Id(Long attendanceTable_id);

    Integer countAllByAttendanceTable_IdAndDate(Long attendanceTable_id, LocalDate date);
    DayDetails findByAttendanceTable_IdAndDate(Long attendanceTable_id, LocalDate date);
}
