package com.spring.Employee.COMMANDS;

import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class EmployeeModificationByLoggedUserCommand implements IEmployeeInfoCommand {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;

    public EmployeeModificationByLoggedUserCommand() {
    }

    public EmployeeModificationByLoggedUserCommand(String firstName, String lastName, Date birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
        System.out.println(employee);
    }
}
