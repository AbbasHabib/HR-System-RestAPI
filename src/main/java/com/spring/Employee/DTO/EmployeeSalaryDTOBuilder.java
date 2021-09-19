package com.spring.Employee.DTO;

import com.spring.Employee.Employee;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class EmployeeSalaryDTOBuilder {
    private LocalDate infoDate;
    private Float grossSalary;
    private Float netSalary;
    private Integer numberOfAbsencesInMonth;
    private Integer numberOfAbsencesThroughYear;
    private Integer allowedAbsences;
    private Integer exceededBy;

    public EmployeeSalaryDTOBuilder() {
    }

    public EmployeeSalaryDTO Build()
    {
        EmployeeSalaryDTO employeeSalaryDTO= new EmployeeSalaryDTO();
        ModelMapperGen.getModelMapperSingleton().map(this, employeeSalaryDTO);
        return employeeSalaryDTO;
    }

    public EmployeeSalaryDTOBuilder setInfoDate(LocalDate infoDate) {
        this.infoDate = infoDate;
        return this;
    }

    public EmployeeSalaryDTOBuilder setAllowedAbsencesThroughYear(Integer allowedAbsences) {
        this.allowedAbsences = allowedAbsences;
        return this;
    }

    public EmployeeSalaryDTOBuilder setNumberOfAbsencesInMonth(Integer numberOfAbsencesInMonth) {
        this.numberOfAbsencesInMonth = numberOfAbsencesInMonth;
        return this;
    }

    public EmployeeSalaryDTOBuilder setNumberOfAbsencesThroughYear(Integer numberOfAbsencesThroughYear) {
        this.numberOfAbsencesThroughYear = numberOfAbsencesThroughYear;
        return this;
    }

    public EmployeeSalaryDTOBuilder setExceededBy(Integer exceededBy) {
        this.exceededBy = exceededBy;
        return this;
    }

    public EmployeeSalaryDTOBuilder setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
        return this;
    }

    public EmployeeSalaryDTOBuilder setNetSalary(Float netSalary) {
        this.netSalary = netSalary;
        return this;
    }


    public Integer getNumberOfAbsencesThroughYear() {
        return numberOfAbsencesThroughYear;
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public Float getNetSalary() {
        return netSalary;
    }

    public Integer getAllowedAbsences() {
        return allowedAbsences;
    }

    public Integer getExceededBy() {
        return exceededBy;
    }
}
