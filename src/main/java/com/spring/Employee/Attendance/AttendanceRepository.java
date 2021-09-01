package com.spring.Employee.Attendance;

import com.spring.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository  extends JpaRepository<AttendanceTable, Long>
{
    Optional<AttendanceTable> findByEmployeeId(Long employee_id);
}
