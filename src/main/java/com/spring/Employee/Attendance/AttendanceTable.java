package com.spring.Employee.Attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.Employee;
import com.spring.Employee.SalariesYearsConstants;

import javax.persistence.*;
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

    private Integer initialWorkingYears = 0;
    private Integer salaryRaise = 0;


    public void addMonthAndDayDetails(DayDetails dayDetails, MonthDetails monthDetails) // make sure that this month doesn't exist already
    {
        if (this.dailyDetailsList == null) this.dailyDetailsList = new ArrayList<>();
        if (this.monthDetailsList == null) this.monthDetailsList = new ArrayList<>();

        this.dailyDetailsList.add(dayDetails);
        this.monthDetailsList.add(monthDetails);
    }

    public void addDay(DayDetails dayDetails)
    {
        if (this.dailyDetailsList == null)
            this.dailyDetailsList = new ArrayList<>();
        this.dailyDetailsList.add(dayDetails);
    }


    public Integer getPermittedAbsenceDays(int workingYears)
    {
        return (workingYears < SalariesYearsConstants.SENIOR_YEARS) ? SalariesYearsConstants.AVAILABLE_ABSENCES_JUNIOR : SalariesYearsConstants.AVAILABLE_ABSENCES_SENIOR;
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



    public List<MonthDetails> getMonthDetailsList()
    {
        return monthDetailsList;
    }

    public void setMonthDetailsList(List<MonthDetails> monthDetailsList)
    {
        this.monthDetailsList = monthDetailsList;
    }

    public void setInitialWorkingYears(Integer initialWorkingYears)
    {
        this.initialWorkingYears = initialWorkingYears;
    }

    public Integer getSalaryRaise()
    {
        return salaryRaise;
    }

    public void setSalaryRaise(Integer salaryRaise)
    {
        this.salaryRaise = salaryRaise;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getInitialWorkingYears()
    {
        return initialWorkingYears;
    }


}