package com.spring.Employee.Attendance;

import com.spring.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository  extends JpaRepository<AttendanceTable, Long>
{
}
