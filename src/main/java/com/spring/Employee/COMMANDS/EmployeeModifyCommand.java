package com.spring.Employee.COMMANDS;

import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Team.Team;
import com.spring.interfaces.IEmployeeInfoCommand;
import com.spring.modelMapperGen.ModelMapperGen;
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
