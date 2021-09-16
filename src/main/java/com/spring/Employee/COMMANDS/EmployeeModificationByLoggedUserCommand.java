package com.spring.Employee.COMMANDS;

import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.Date;

public class EmployeeModificationByLoggedUserCommand {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;

    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
        System.out.println(employee);
    }

    public EmployeeModificationByLoggedUserCommand() {
    }

    public EmployeeModificationByLoggedUserCommand(String firstName, String lastName, Date birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
