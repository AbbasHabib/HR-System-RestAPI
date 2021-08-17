package com.spring.Employee;

import com.spring.Employee.dto.EmployeeModifyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee addEmployee(@RequestBody Employee employee) throws Exception
    {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/")
    public List<Employee> getEmployees()
    {
        return employeeService.getEmployees();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable String id) // send path parameter
    {
        return employeeService.getEmployee(Long.parseLong(id));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteEmployee(@PathVariable String id)
    {
        return employeeService.deleteEmployee(Long.parseLong(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee modifyEmployee(@PathVariable String id, @RequestBody EmployeeModifyDto employeeDto)
    {
        return employeeService.modifyEmployee(Long.parseLong(id), employeeDto);
    }

}
