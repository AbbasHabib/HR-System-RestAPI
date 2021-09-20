package com.hrsystem.employee.dtos;

import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.utilities.interfaces.IEmployeeInfoDTO;
import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePublicInfoDTO implements IEmployeeInfoDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private EmployeeRole role;


    public EmployeePublicInfoDTO() {
    }

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
    }
}
