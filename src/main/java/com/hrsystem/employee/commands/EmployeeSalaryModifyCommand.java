package com.hrsystem.employee.commands;

import com.hrsystem.employee.Employee;
import com.hrsystem.utilities.interfaces.IEmployeeInfoCommand;
import com.hrsystem.utilities.ModelMapperGen;
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
