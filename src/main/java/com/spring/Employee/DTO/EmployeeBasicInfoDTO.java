package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeBasicInfoDTO implements IEmployeeInfoDTO, IdOwner {
    private Long id;
    private String lastName;
    private String firstName;
    private String birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }


    public List<EmployeeBasicInfoDTO> generateDTOListFromEmployeeList(List<Employee> employees) {
        List<EmployeeBasicInfoDTO> employeeBasicInfoDTOList = new ArrayList<>();
        int i = 0;
        for (Employee employee : employees) {
            employeeBasicInfoDTOList.add(new EmployeeBasicInfoDTO());
            employeeBasicInfoDTOList.get(i++).setEmployeeToDTO(employee);
        }
        return employeeBasicInfoDTOList;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
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
