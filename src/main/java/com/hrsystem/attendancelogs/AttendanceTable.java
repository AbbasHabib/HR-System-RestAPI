package com.hrsystem.attendancelogs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrsystem.attendancelogs.monthdetails.MonthDetails;
import com.hrsystem.employee.Employee;
import com.hrsystem.attendancelogs.daydetails.DayDetails;
import com.hrsystem.utilities.interfaces.constants.SalariesYearsConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance_table")

@Getter
@Setter
public class AttendanceTable {
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

    public AttendanceTable(Employee employee) {
        this.employee = employee;
    }

    public AttendanceTable() {
    }

    public void addMonthAndDayDetails(DayDetails dayDetails, MonthDetails monthDetails) // make sure that this month doesn't exist already
    {
        if (this.dailyDetailsList == null) this.dailyDetailsList = new ArrayList<>();
        if (this.monthDetailsList == null) this.monthDetailsList = new ArrayList<>();

        this.dailyDetailsList.add(dayDetails);
        this.monthDetailsList.add(monthDetails);
    }

    public void addDay(DayDetails dayDetails) {
        if (this.dailyDetailsList == null)
            this.dailyDetailsList = new ArrayList<>();
        this.dailyDetailsList.add(dayDetails);
    }

    public Integer getPermittedAbsenceDays(int workingYears) {
        return (workingYears < SalariesYearsConstants.SENIOR_YEARS) ? SalariesYearsConstants.AVAILABLE_ABSENCES_JUNIOR : SalariesYearsConstants.AVAILABLE_ABSENCES_SENIOR;
    }

}