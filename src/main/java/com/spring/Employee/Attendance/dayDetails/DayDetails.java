package com.spring.Employee.Attendance.dayDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.interfaces.IdOwner;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "day_details")
public class DayDetails implements IdOwner
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable attendanceTable;

    private LocalDate date;
    private boolean absent = false;
    private Float bonusInSalary;

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

    public Float getBonusInSalary()
    {
        return bonusInSalary;
    }

    public void setBonusInSalary(Float bonusInSalary)
    {
        this.bonusInSalary = bonusInSalary;
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
