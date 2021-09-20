package com.hrsystem.employee.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrsystem.department.Department;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.utilities.interfaces.IEmployeeInfoDTO;
import com.hrsystem.utilities.ModelMapperGen;
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
