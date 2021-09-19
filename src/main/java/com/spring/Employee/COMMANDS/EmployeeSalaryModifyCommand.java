package com.spring.Employee.COMMANDS;

import com.spring.Employee.Employee;
import com.spring.modelMapperGen.ModelMapperGen;

public class EmployeeSalaryModifyCommand implements IEmployeeInfoCommand {
    private Float grossSalary;
    private Float salaryRaise;


    @Override
    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Float getSalaryRaise() {
        return salaryRaise;
    }

    public void setSalaryRaise(Float salaryRaise) {
        this.salaryRaise = salaryRaise;
    }
}
