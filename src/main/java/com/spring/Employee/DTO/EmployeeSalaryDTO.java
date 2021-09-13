package com.spring.Employee.DTO;

import com.spring.Employee.Employee;

import java.time.LocalDate;

public class EmployeeSalaryDTO {
    private LocalDate infoDate;
    private Float grossSalary;
    private Float netSalary;
    private Integer numberOfAbsencesInMonth;
    private Integer numberOfAbsencesThroughYear;
    private Integer allowedAbsences;
    private Integer exceededBy;

    public EmployeeSalaryDTO() {
    }

    public EmployeeSalaryDTO(Employee employee, LocalDate inoDate) {
        infoDate = inoDate;
        this.grossSalary = employee.getGrossSalary();
        this.netSalary = employee.getNetSalary();
    }

    public LocalDate getInfoDate() {
        return infoDate;
    }


    public EmployeeSalaryDTO setInfoDate(LocalDate infoDate) {
        this.infoDate = infoDate;
        return this;
    }

    public EmployeeSalaryDTO setAllowedAbsencesThroughYear(Integer allowedAbsences) {
        this.allowedAbsences = allowedAbsences;
        return this;
    }

    public EmployeeSalaryDTO setNumberOfAbsencesInMonth(Integer numberOfAbsencesInMonth) {
        this.numberOfAbsencesInMonth = numberOfAbsencesInMonth;
        return this;
    }

    public Integer getNumberOfAbsencesThroughYear() {
        return numberOfAbsencesThroughYear;
    }

    public EmployeeSalaryDTO setNumberOfAbsencesThroughYear(Integer numberOfAbsencesThroughYear) {
        this.numberOfAbsencesThroughYear = numberOfAbsencesThroughYear;
        return this;
    }

    public Integer getAllowedAbsences() {
        return allowedAbsences;
    }

    public Integer getExceededBy() {
        return exceededBy;
    }

    public EmployeeSalaryDTO setExceededBy(Integer exceededBy) {
        this.exceededBy = exceededBy;
        return this;
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public EmployeeSalaryDTO setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
        return this;
    }

    public Float getNetSalary() {
        return netSalary;
    }

    public EmployeeSalaryDTO setNetSalary(Float netSalary) {
        this.netSalary = netSalary;
        return this;
    }
}
