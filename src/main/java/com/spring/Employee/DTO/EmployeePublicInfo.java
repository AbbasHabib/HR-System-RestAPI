package com.spring.Employee.DTO;

import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.interfaces.IEmployeeInfoDTO;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePublicInfo implements IEmployeeInfoDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private EmployeeRole role;


    public EmployeePublicInfo() {
    }

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }
}
