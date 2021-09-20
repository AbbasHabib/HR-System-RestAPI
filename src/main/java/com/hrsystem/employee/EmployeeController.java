package com.hrsystem.employee;

import com.hrsystem.employee.commands.EmployeeModificationByLoggedUserCommand;
import com.hrsystem.employee.commands.EmployeeModifyCommand;
import com.hrsystem.employee.commands.EmployeeSalaryModifyCommand;
import com.hrsystem.employee.dtos.EmployeeBasicInfoDTO;
import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "/employee/", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO addEmployee(@RequestBody Employee employee) throws Exception {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/employee/")
    public List<EmployeeBasicInfoDTO> getEmployees() {
        return employeeService.getEmployeesInDTO();
    }

    @GetMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO getEmployee(@PathVariable String id) throws CustomException // send path parameter
    {
        return employeeService.getEmployeeInDTO(Long.parseLong(id));
    }

    @DeleteMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteEmployee(@PathVariable String id) throws CustomException {
        return employeeService.deleteEmployee(Long.parseLong(id));
    }

    @PutMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO modifyEmployee(@PathVariable String id, @RequestBody EmployeeModifyCommand employeeModifyCommand) throws CustomException, IllegalAccessException {
        return employeeService.modifyEmployee(Long.parseLong(id), employeeModifyCommand);
    }

    @PutMapping(value = "/employee/{id}/salary", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO modifyEmployeeSalary(@PathVariable String id, @RequestBody EmployeeSalaryModifyCommand employeeModifyCommand) throws CustomException, IllegalAccessException {
        return employeeService.modifyEmployeeSalary(Long.parseLong(id), employeeModifyCommand);
    }

    @GetMapping("/employee/manager/recursive/{id}")
    public List<EmployeeBasicInfoDTO> getEmployeesUnderManagerRecursively(@PathVariable String id) {
        return employeeService.getManagerEmployeesRecursively(Long.parseLong(id));
    }

    @GetMapping("/employee/manager/{id}")
    public List<EmployeeBasicInfoDTO> getEmployeesUnderManager(@PathVariable String id) throws CustomException {
        return employeeService.getManagerEmployees(Long.parseLong(id));
    }


    @GetMapping(value = "/profile/employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO getEmployeeByLoggedUser() throws CustomException {
        EmployeeInfoDTO employeeDto = employeeService.getEmployeeInDTOByUserFromAuthentication();
        if (employeeDto == null)
            throw new CustomException("this user Id does not exits");
        return employeeDto;
    }


    @PutMapping(value = "/profile/employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeInfoDTO modifyEmployeeByLoggedUSer(@RequestBody EmployeeModificationByLoggedUserCommand employeeModifyCommand) throws CustomException, IllegalAccessException {
        return employeeService.modifyEmployeeByLoggedUser(employeeModifyCommand);
    }

    @GetMapping(value = "/profile/employee/all-sub-employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeBasicInfoDTO> getEmployeesUnderManagerByLoggedUser() throws CustomException {
        return employeeService.getManagerEmployeesByLoggedUser();
    }

}
