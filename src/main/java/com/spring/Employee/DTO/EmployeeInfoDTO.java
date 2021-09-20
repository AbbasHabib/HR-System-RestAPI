package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Security.EmployeeRole;
import com.spring.interfaces.IEmployeeInfoDTO;
import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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
    private Float salaryRaise;
    private EmployeePublicInfo managerPublicInfo;
    private List<EmployeeBasicInfoDTO> SubEmployeesBasicInfo;

    public static List<EmployeeInfoDTO> setEmployeeToDTOList(List<Employee> employees) {
        List<EmployeeInfoDTO> employeesDTO = new ArrayList<>();
        for (Employee emp : employees) {
            EmployeeInfoDTO empDTO = new EmployeeInfoDTO();
            empDTO.setEmployeeToDTO(emp);
            employeesDTO.add(empDTO);
        }
        return employeesDTO;
    }

    public void setEmployeeToDTO(Employee employeeFullData) {
        ModelMapperGen.getModelMapperSingleton().map(employeeFullData, this);
        if (employeeFullData.getManager() != null) {
            managerPublicInfo = new EmployeePublicInfo();
            managerPublicInfo.setEmployeeToDTO(employeeFullData.getManager());
        }
        if (employeeFullData.getSubEmployees() != null) {
            if (employeeFullData.getSubEmployees().size() > 0) {
                int i = 0;
                SubEmployeesBasicInfo = new ArrayList<EmployeeBasicInfoDTO>();

                for (Employee subEmployee : employeeFullData.getSubEmployees()) {
                    SubEmployeesBasicInfo.add(new EmployeeBasicInfoDTO());
                    ModelMapperGen.getModelMapperSingleton().map(subEmployee, SubEmployeesBasicInfo.get(i++));
                }
            }
        }
    }
}
