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

    private Integer workingYears;

    public void addNewDayInfo(DayDetails dayDetailsDetail)
    {
        if (dailyDetailsList == null)
            dailyDetailsList = new ArrayList<>();
        dayDetailsDetail.setAttendanceTable(this);
//        calcTimeLogic(dayDetailsDetail.getDate());
        dailyDetailsList.add(new DayDetails(dayDetailsDetail.getDate()));
    }

//    private DayDetails mostRecentAttendanceData()
//    {
//        if (dailyDetailsList.size() < 1)
//            return null;
//        return dailyDetailsList.get(dailyDetailsList.size() - 1);
//    }
//
//    private void calcTimeLogic(LocalDate newlyAddedDate)
//    {
//        DayDetails mostRecentDate = mostRecentAttendanceData();
//        if (mostRecentDate == null)
//            return;
//        Period period = Period.between(mostRecentDate.getDate(), newlyAddedDate);
//        if (period.getYears() >= 1)
//            workingYears += period.getYears();
//    }

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