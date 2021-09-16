package com.spring.Employee.DTO;

import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.modelMapperGen.ModelMapperGen;

import java.time.LocalDate;
import java.util.Date;

public class EmployeeBasicInfoDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String graduationDate;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Float getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Float netSalary) {
        this.netSalary = netSalary;
    }
}
