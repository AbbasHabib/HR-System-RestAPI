package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "attendance_table")
public class AttendanceTable
{
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Employee user;
    private Integer workingYears;
    private Integer permittedAbsence;
    private Integer absenceDaysInCurrentYear;
    private Integer absenceDaysInCurrentMonth;

    public AttendanceTable(Long id, Employee user, Integer workingYears, Integer permittedAbsence, Integer absenceDaysInCurrentYear, Integer absenceDaysInCurrentMonth)
    {
        this.id = id;
        this.user = user;
        this.workingYears = workingYears;
        this.permittedAbsence = permittedAbsence;
        this.absenceDaysInCurrentYear = absenceDaysInCurrentYear;
        this.absenceDaysInCurrentMonth = absenceDaysInCurrentMonth;
    }

    private void addAbsenceInYear(Integer absenceDays)
    {
        this.absenceDaysInCurrentYear += absenceDays;
    }

    public void addAbsenceInMonth(Integer absenceDays)
    {
        this.absenceDaysInCurrentMonth += absenceDays;
        if (this.absenceDaysInCurrentMonth > this.getCurrentMonthDays()) // if input was above what is expected
            this.absenceDaysInCurrentMonth = this.getCurrentMonthDays();
        addAbsenceInYear(absenceDays);
    }

    private Integer getPermittedAbsenceDays()
    {
        return (workingYears < SalariesYearsConstants.SENIOR_YEARS)
                ? SalariesYearsConstants.AVAILABLE_ABSENCES_JUNIOR
                : SalariesYearsConstants.AVAILABLE_ABSENCES_SENIOR;
    }

    public Integer getOverAbsenceInCurrentMonth()
    {
        return (this.absenceDaysInCurrentYear > this.getPermittedAbsenceDays())
                ? this.absenceDaysInCurrentMonth
                : 0;
    }

    public void passAYear()
    {
        workingYears += 1;
        resetYearAbsence();
        resetMonthAbsence();
    }


    private void resetMonthAbsence()
    {
        this.absenceDaysInCurrentMonth = 0;
    }

    private void resetYearAbsence()
    {
        this.absenceDaysInCurrentYear = 0;
    }

    private Integer getCurrentMonthDays()
    {
        Calendar currentDate = Calendar.getInstance();
        return currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
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

    public Employee getUser()
    {
        return user;
    }

    public void setUser(Employee user)
    {
        this.user = user;
    }

    public Integer getWorkingYears()
    {
        return workingYears;
    }

    public void setWorkingYears(Integer workingYears)
    {
        this.workingYears = workingYears;
    }

    public Integer getPermittedAbsence()
    {
        return permittedAbsence;
    }

    public void setPermittedAbsence(Integer permittedAbsence)
    {
        this.permittedAbsence = permittedAbsence;
    }

    public Integer getAbsenceDaysInCurrentYear()
    {
        return absenceDaysInCurrentYear;
    }

    public void setAbsenceDaysInCurrentYear(Integer absenceDaysInCurrentYear)
    {
        this.absenceDaysInCurrentYear = absenceDaysInCurrentYear;
    }

    public Integer getAbsenceDaysInCurrentMonth()
    {
        return absenceDaysInCurrentMonth;
    }

    public void setAbsenceDaysInCurrentMonth(Integer absenceDaysInCurrentMonth)
    {
        this.absenceDaysInCurrentMonth = absenceDaysInCurrentMonth;
    }
}
