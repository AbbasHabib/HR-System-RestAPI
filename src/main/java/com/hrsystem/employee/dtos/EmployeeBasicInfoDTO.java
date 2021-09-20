package com.hrsystem.employee.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.utilities.interfaces.IEmployeeInfoDTO;
import com.hrsystem.utilities.interfaces.IdOwner;
import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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
}
