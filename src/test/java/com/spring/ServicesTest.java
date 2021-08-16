package com.spring;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = HrSystemApplication.class)
public class ServicesTest
{
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void addEmployeeReturnEmployee()
    {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setName("Ahmed");
        // expected to get same (employeeToAdd) object at (resultReturned)
        Employee resultReturned = employeeService.addEmployee(employeeToAdd);

        assertEquals(resultReturned, employeeToAdd); // compare the two objects
    }

}
