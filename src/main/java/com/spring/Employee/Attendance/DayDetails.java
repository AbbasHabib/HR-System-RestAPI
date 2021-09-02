package com.spring.Employee.Attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leaves_days")
public class DayDetails
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    private boolean absent = false;
    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable attendanceTable;

    private Float bonusSalary;

    public DayDetails(LocalDate date)
    {
        this.date = date;
    }

    public boolean isAbsent()
    {
        return absent;
    }

    public void setAbsent(boolean absent)
    {
        this.absent = absent;
    }

    public Float getBonusSalary()
    {
        return bonusSalary;
    }

    public void setBonusSalary(Float bonusSalary)
    {
        this.bonusSalary = bonusSalary;
    }

    public DayDetails() { }

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

    public AttendanceTable getAttendanceTable()
    {
        return attendanceTable;
    }

    public void setAttendanceTable(AttendanceTable attendanceTable)
    {
        this.attendanceTable = attendanceTable;
    }

}
