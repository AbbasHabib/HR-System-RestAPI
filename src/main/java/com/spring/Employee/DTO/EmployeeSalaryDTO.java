package com.spring.Employee.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    public EmployeeSalaryDTO(LocalDate infoDate, Float grossSalary, Float netSalary, Integer numberOfAbsencesInMonth, Integer numberOfAbsencesThroughYear, Integer allowedAbsences, Integer exceededBy) {
        this.infoDate = infoDate;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
        this.numberOfAbsencesInMonth = numberOfAbsencesInMonth;
        this.numberOfAbsencesThroughYear = numberOfAbsencesThroughYear;
        this.allowedAbsences = allowedAbsences;
        this.exceededBy = exceededBy;
    }
}
