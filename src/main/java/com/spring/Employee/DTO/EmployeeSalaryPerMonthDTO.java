package com.spring.Employee.DTO;


import java.time.LocalDate;

public class EmployeeSalaryPerMonthDTO {
    private Float grossSalaryOfMonth;
    private LocalDate date;
    private Integer absences;
    private Float bonuses;

    private Integer absencesInYear;

    public Float getGrossSalaryOfMonth() {
        return grossSalaryOfMonth;
    }

    public void setGrossSalaryOfMonth(Float grossSalaryOfMonth) {
        this.grossSalaryOfMonth = grossSalaryOfMonth;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAbsences() {
        return absences;
    }

    public void setAbsences(Integer absences) {
        this.absences = absences;
    }

    public Float getBonuses() {
        return bonuses;
    }

    public void setBonuses(Float bonuses) {
        this.bonuses = bonuses;
    }

    public Integer getAbsencesInYear() {
        return absencesInYear;
    }

    public void setAbsencesInYear(Integer absencesInYear) {
        this.absencesInYear = absencesInYear;
    }
}
