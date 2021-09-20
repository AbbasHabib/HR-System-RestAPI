package com.spring.Employee.COMMANDS;

import com.spring.Employee.Employee;
import com.spring.interfaces.IEmployeeInfoCommand;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSalaryModifyCommand implements IEmployeeInfoCommand {
    private Float grossSalary;
    private Float salaryRaise;

    @Override
    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
    }

}
