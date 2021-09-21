package com.hrsystem.employee.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrsystem.department.Department;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.team.Team;
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
public class EmployeeInfoDTO implements IdOwner, IEmployeeInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    private Department department;
    private Team team;
    private Gender gender;
    private Float grossSalary;
    private Float netSalary;
    private EmployeeRole role;
    private Float salaryRaise;
    private EmployeePublicInfoDTO managerPublicInfo;
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
            managerPublicInfo = new EmployeePublicInfoDTO();
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
