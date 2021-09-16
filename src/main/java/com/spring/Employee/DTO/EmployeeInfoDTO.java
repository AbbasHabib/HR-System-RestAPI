package com.spring.Employee.DTO;

import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String graduationDate;
    private Department department;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;
    private EmployeeRole role;
//    private EmployeeBasicInfoDTO managerBasicInfo;
//    private List<EmployeeInfoDTO> SubEmployeesBasicInfo;

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
//        ModelMapperGen.getModelMapperSingleton().map(employeeFullData.getManager(), this.managerBasicInfo);
//        ModelMapperGen.getModelMapperSingleton().map(employeeFullData.getSubEmployees(), DTO.SubEmployeesBasicInfo);

    }

    public static List<EmployeeInfoDTO> setEmployeeToDTOList(List<Employee> employees) {
        List<EmployeeInfoDTO> employeesDTO = new ArrayList<>();
        for (Employee emp : employees) {
            EmployeeInfoDTO empDTO = new EmployeeInfoDTO();
            empDTO.setEmployeeToDTO(emp);
            employeesDTO.add(empDTO);
        }
        return employeesDTO;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

//    public EmployeeBasicInfoDTO getManagerBasicInfo() {
//        return managerBasicInfo;
//    }
//
//    public void setManagerBasicInfo(EmployeeBasicInfoDTO managerBasicInfo) {
//        this.managerBasicInfo = managerBasicInfo;
//    }

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

    public Gender getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
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
