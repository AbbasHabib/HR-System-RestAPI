package com.spring.Employee.DTO;

import com.spring.Employee.Employee;
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
}
