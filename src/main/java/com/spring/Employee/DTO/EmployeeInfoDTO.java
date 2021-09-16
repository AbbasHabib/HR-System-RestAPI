package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeInfoDTO implements IdOwner, IEmployeeInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    private Department department;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;
    private EmployeeRole role;
    private EmployeePublicInfo managerPublicInfo;
    private List<EmployeeBasicInfoDTO> SubEmployeesBasicInfo;

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
        if (employeeFullData.getManager() != null) {
            managerPublicInfo = new EmployeePublicInfo();
            managerPublicInfo.setEmployeeToDTO(employeeFullData.getManager());
        }
        if (employeeFullData.getSubEmployees().size() > 0) {
            int i = 0;
            SubEmployeesBasicInfo = new ArrayList<EmployeeBasicInfoDTO>();

            for (Employee subEmployee : employeeFullData.getSubEmployees()) {
                SubEmployeesBasicInfo.add(new EmployeeBasicInfoDTO());
                ModelMapperGen.getModelMapperSingleton().map(subEmployee, SubEmployeesBasicInfo.get(i++));
            }
        }
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



    public EmployeePublicInfo getManagerPublicInfo() {
        return managerPublicInfo;
    }

    public void setManagerPublicInfo(EmployeePublicInfo managerPublicInfo) {
        this.managerPublicInfo = managerPublicInfo;
    }

    public List<EmployeeBasicInfoDTO> getSubEmployeesBasicInfo() {
        return SubEmployeesBasicInfo;
    }

    public void setSubEmployeesBasicInfo(List<EmployeeBasicInfoDTO> subEmployeesBasicInfo) {
        SubEmployeesBasicInfo = subEmployeesBasicInfo;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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
