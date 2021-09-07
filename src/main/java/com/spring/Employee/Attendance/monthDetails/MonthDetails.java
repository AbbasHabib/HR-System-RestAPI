package com.spring.Employee.Attendance.monthDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Attendance.AttendanceTable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "month_details")
public class MonthDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable attendanceTable;

    private Float grossSalaryOfMonth = null;

    private LocalDate date; // (YYYY, MM, 1)
    private Integer absences = 0;
    private Float bonuses = 0F;



    public MonthDetails(LocalDate date)
    {
        this();
        this.date = date;
    }

    public MonthDetails()
    {
    }


    public Float getGrossSalaryOfMonth()
    {
        return grossSalaryOfMonth;
    }

    public void setGrossSalaryOfMonth(Float salary)
    {
        this.grossSalaryOfMonth = salary;
    }

    public void addAbsence(int n)
    {
        this.absences += n;
    }

    public void addBonus(float n)
    {
        this.bonuses += n;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public AttendanceTable getAttendanceTable()
    {
        return attendanceTable;
    }

    public void setAttendanceTable(AttendanceTable attendanceTable)
    {
        this.attendanceTable = attendanceTable;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public Integer getAbsences()
    {
        return absences;
    }

    public void setAbsences(Integer absences)
    {
        this.absences = absences;
    }

    public Float getBonuses()
    {
        return bonuses;
    }

    public void setBonuses(Float bonuses)
    {
        this.bonuses = bonuses;
    }
}
