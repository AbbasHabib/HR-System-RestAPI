package com.spring.Employee.DTO;

import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.modelMapperGen.ModelMapperGen;

public class EmployeePublicInfo implements IEmployeeInfoDTO{
    private String firstName;
    private String lastName;
    private Gender gender;
    private EmployeeRole role;


    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }


    public EmployeePublicInfo() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
}
