package com.hrsystem.employee.dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeSalaryPerMonthDTO {
    private Float grossSalaryOfMonth;
    private LocalDate date;
    private Integer absences;
    private Float bonuses;
}
