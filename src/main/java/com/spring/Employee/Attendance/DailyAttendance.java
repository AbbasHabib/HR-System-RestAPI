package com.spring.Employee.Attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_attendance")
@IdClass(DailyAttendanceCompositeKey.class)
public class DailyAttendance
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Id
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable currentAttendanceTable;

    boolean attended = true;

    public DailyAttendance(LocalDate date, boolean attended)
    {
        this.date = date;
        this.attended = attended;
    }


    public DailyAttendance() { }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public AttendanceTable getCurrentAttendanceTable()
    {
        return currentAttendanceTable;
    }

    public void setCurrentAttendanceTable(AttendanceTable attendanceTable)
    {
        this.currentAttendanceTable = attendanceTable;
    }

    public boolean isAttended()
    {
        return attended;
    }

    public void setAttended(boolean attended)
    {
        this.attended = attended;
    }
}
