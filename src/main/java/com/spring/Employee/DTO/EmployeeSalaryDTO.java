package com.spring.Employee.DTO;

import com.spring.Employee.Employee;

public class EmployeeSalaryDTO
{
    private Float grossSalary;
    private Float netSalary;

    public EmployeeSalaryDTO()
    {
    }

    public EmployeeSalaryDTO(Employee employee)
    {

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
