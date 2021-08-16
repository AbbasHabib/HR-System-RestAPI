package com.spring.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) throws Exception
    {
        if(this.getEmployee(employee.getId()) == null)
        {
            return employeeRepository.save(employee);
        }
        throw new Exception("user ID already exists");
    }
    public Employee saveEmployeeModification(Employee e)
    {
        return employeeRepository.save(e);
    }
    public Boolean deleteEmployee(Long employeeId)
    {
        if(this.getEmployee(employeeId) != null)
        {
            employeeRepository.deleteById(employeeId);
            return true;
        }
        return false;
    }


    public List<Employee> getEmployees()
    {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(Long employeeId) // send path parameter
    {
        return employeeRepository.findById(employeeId).isPresent() ? employeeRepository.findById(employeeId).get() : null;
    }

}
