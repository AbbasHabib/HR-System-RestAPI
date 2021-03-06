package com.hrsystem.employee.dtos;

import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;

import java.time.LocalDate;

@Getter
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

    public EmployeeSalaryDTO build()
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
}
