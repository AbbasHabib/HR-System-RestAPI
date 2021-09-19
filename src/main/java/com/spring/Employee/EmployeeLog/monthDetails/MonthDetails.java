package com.spring.Employee.EmployeeLog.monthDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.EmployeeLog.AttendanceTable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "month_details")
@Getter
@Setter
public class MonthDetails {
    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable attendanceTable;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float grossSalaryOfMonth = null;

    private LocalDate date; // (YYYY, MM, 1)
    private Integer absences = 0;
    private Float bonuses = 0F;


    public MonthDetails(LocalDate date) {
        this.date = date;
    }

    public MonthDetails() {
    }

    public void addAbsence(int n) {
        this.absences += n;
    }

    public void addBonus(float n) {
        this.bonuses += n;
    }
}
