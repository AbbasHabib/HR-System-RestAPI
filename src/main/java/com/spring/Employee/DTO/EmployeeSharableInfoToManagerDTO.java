package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmployeeSharableInfoToManagerDTO implements IEmployeeInfoDTO {
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

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }

}
