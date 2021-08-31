package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_attendance")
@IdClass(DailyAttendanceCompositeKey.class)
public class DailyAttendance
{
    @Id
    @Column(name = "id")
    private Long id;
    @Id
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    @JsonIgnore
    AttendanceTable attendanceTable;

    boolean attended = false;
}
