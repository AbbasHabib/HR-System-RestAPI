package com.spring.Employee;

import com.spring.Security.EmployeeRole;
import com.spring.modelMapperGen.ModelMapperGen;

import java.lang.reflect.Field;
import java.util.Date;

public class EmployeeNotNullableFields {
    private String nationalId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date graduationDate;
    private Float grossSalary;
    private EmployeeRole role;


    public EmployeeNotNullableFields(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(employee, this);

    }

    public EmployeeNotNullableFields() {
    }

    public void mapFromEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(employee, this);
    }

    public String checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return f.getName();
        return "";
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
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

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
}
