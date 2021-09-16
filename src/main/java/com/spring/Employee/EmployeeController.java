package com.spring.Employee;

import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.ExceptionsCustom.CustomException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "/employee/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee addEmployee(@RequestBody Employee employee) throws Exception, CustomException {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/employee/")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable String id) throws CustomException // send path parameter
    {
        Employee emp = employeeService.getEmployee(Long.parseLong(id));
        if (emp == null)
            throw new CustomException("this user Id does not exits");
        return emp;
    }

    @DeleteMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteEmployee(@PathVariable String id) throws CustomException {
        return employeeService.deleteEmployee(Long.parseLong(id));
    }

    @PutMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee modifyEmployee(@PathVariable String id, @RequestBody EmployeeModifyCommand employeeModifyCommand) throws NotFoundException, CustomException {
        return employeeService.modifyEmployee(Long.parseLong(id), employeeModifyCommand);
    }


    @GetMapping("/employee/manager/recursive/{id}")
    public List<EmployeeInfoOnlyDTO> getEmployeesUnderManagerRecursively(@PathVariable String id) throws CustomException {
        return employeeService.getManagerEmployeesRecursively(Long.parseLong(id));
    }

    @GetMapping("/employee/manager/{id}")
    public List<EmployeeInfoOnlyDTO> getEmployeesUnderManager(@PathVariable String id) throws CustomException {
        return employeeService.getManagerEmployees(Long.parseLong(id));
    }



    @GetMapping(value = "/profile/employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployeeByLoggedUser() throws CustomException // send path parameter
    {
        Employee emp = employeeService.getEmployeeByUserFromAuthentication();
        if (emp == null)
            throw new CustomException("this user Id does not exits");
        return emp;
    }

}
