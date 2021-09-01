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
    private Long id;

    @OneToOne(mappedBy = "attendanceTable", cascade = CascadeType.ALL)
    @JsonIgnore
    private Employee employee;

    @OneToMany(mappedBy = "currentAttendanceTable") // one manager to many employees
    @JsonIgnore
    private List<DailyAttendance> dailyAttendanceList;

    private Integer workingYears;

    public void addAttendanceData(LocalDate date, boolean attended)
    {
        if (dailyAttendanceList == null)
            dailyAttendanceList = new ArrayList<>();
        dailyAttendanceList.add(new DailyAttendance(date, attended));
        calcTimeLogic(date);
    }

    private DailyAttendance mostRecentAttendanceData()
    {
        return dailyAttendanceList.get(dailyAttendanceList.size() - 1);
    }

    private void calcTimeLogic(LocalDate newlyAddedDate)
    {
        Period period = Period.between(mostRecentAttendanceData().getDate(), newlyAddedDate);
        if(period.getYears() >= 1)
        {
            workingYears += period.getYears();
        }
    }

    public Integer calcMonthDays(YearMonth yearMonth)    //YearMonth.of(1999, 2);
    {
        return yearMonth.lengthOfMonth();
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

    public List<DailyAttendance> getDailyAttendanceList()
    {
        return dailyAttendanceList;
    }

    public void setDailyAttendanceList(List<DailyAttendance> dailyAttendanceList)
    {
        this.dailyAttendanceList = dailyAttendanceList;
    }

    public AttendanceTable() { }

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