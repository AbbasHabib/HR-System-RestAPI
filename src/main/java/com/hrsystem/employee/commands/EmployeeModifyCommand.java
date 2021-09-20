package com.hrsystem.employee.commands;

import com.hrsystem.department.Department;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.team.Team;
import com.hrsystem.utilities.interfaces.IEmployeeInfoCommand;
import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class EmployeeModifyCommand implements IEmployeeInfoCommand {
    private String firstName;
    private String lastName;
    private Team team;
    private Date birthDate;
    private Date graduationDate;
    private Gender gender;
    private Department department;
    private Employee manager;
    private Set<Employee> employees;
    private Float grossSalary;

    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
    }
}
