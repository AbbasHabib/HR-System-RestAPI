package com.spring.Employee.DTO;

import com.spring.Employee.Employee;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeSalaryDTO
{
    private LocalDate infoDate;
    private Float grossSalary;
    private Float netSalary;

    public EmployeeSalaryDTO()
    {
    }

    public EmployeeSalaryDTO(Employee employee, LocalDate inoDate)
    {
        infoDate = inoDate;
        this.grossSalary = employee.getGrossSalary();
        this.netSalary = employee.getNetSalary();
    }

    public Float getGrossSalary()
    {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary)
    {
        this.grossSalary = grossSalary;
    }

    public Float getNetSalary()
    {
        return netSalary;
    }

    public void setNetSalary(Float netSalary)
    {
        this.netSalary = netSalary;
    }
}
