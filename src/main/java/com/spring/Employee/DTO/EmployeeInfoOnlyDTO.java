package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeInfoOnlyDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;
    private EmployeeRole role;

    public static void setEmployeeToDTO(Employee e, EmployeeInfoOnlyDTO DTO) {
        ModelMapperGen.getModelMapperSingleton().map(e, DTO);
    }

    public static List<EmployeeInfoOnlyDTO> setEmployeeToDTOList(List<Employee> employees) {
        List<EmployeeInfoOnlyDTO> employeesDTO = new ArrayList<>();
        for (Employee emp : employees) {
            EmployeeInfoOnlyDTO empDTO = new EmployeeInfoOnlyDTO();
            setEmployeeToDTO(emp, empDTO);
            employeesDTO.add(empDTO);
        }
        return employeesDTO;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
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

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
}
