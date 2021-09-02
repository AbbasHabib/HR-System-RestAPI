package com.spring.Employee.Attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;
import com.spring.Employee.SalariesYearsConstants;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.*;

@Entity
@Table(name = "attendance_table")

public class AttendanceTable
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @OneToOne(mappedBy = "attendanceTable", cascade = CascadeType.ALL)
    @JsonIgnore
    private Employee employee;

    @OneToMany(mappedBy = "attendanceTable") // one manager to many employees
    private List<DayDetails> dailyDetailsList;
    @OneToMany(mappedBy = "attendanceTable") // one manager to many employees
    private List<MonthDetails> monthDetailsList;

    private Integer workingYears;



    public void addMonthAndDayDetails(DayDetails dayDetails, MonthDetails monthDetails) // make sure that this month doesn't exist already
    {
        if (this.dailyDetailsList == null)
            this.dailyDetailsList  = new ArrayList<>();
        if(this.monthDetailsList == null)
            this.monthDetailsList  = new ArrayList<>();

        this.dailyDetailsList.add(dayDetails);
        this.monthDetailsList.add(monthDetails);
    }

    public void addDay(DayDetails dayDetails)
    {
        if (this.dailyDetailsList == null)
            this.dailyDetailsList  = new ArrayList<>();
        this.dailyDetailsList.add(dayDetails);
    }


    public Integer getPermittedAbsenceDays()
    {
        return (workingYears < SalariesYearsConstants.SENIOR_YEARS)
                ? SalariesYearsConstants.AVAILABLE_ABSENCES_JUNIOR
                : SalariesYearsConstants.AVAILABLE_ABSENCES_SENIOR;
    }


    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public List<DayDetails> getDailyDetailsList()
    {
        return dailyDetailsList;
    }

    public void setDailyDetailsList(List<DayDetails> dailyAttendanceList)
    {
        this.dailyDetailsList = dailyAttendanceList;
    }

    public AttendanceTable()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getWorkingYears()
    {
        return workingYears;
    }

    public void setWorkingYears(Integer workingYears)
    {
        this.workingYears = workingYears;
    }

}