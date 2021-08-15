package com.spring.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(@RequestBody Employee employee)
    {
        return employeeRepository.save(employee);
    }
    @GetMapping("/view")
    public List<Employee> getEmployees()
    {
        return employeeRepository.findAll();
    }
}
