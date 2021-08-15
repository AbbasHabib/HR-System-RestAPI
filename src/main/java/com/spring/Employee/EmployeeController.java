package com.spring.Employee;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/test")
    public Employee testResponse(@RequestBody Employee d)
    {
        return d;
    }
    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee)
    {
        return employeeService.addEmployee(employee);
    }
    @GetMapping("/view")
    public List<Employee> getEmployees()
    {
        return employeeService.getEmployees();
    }
}
