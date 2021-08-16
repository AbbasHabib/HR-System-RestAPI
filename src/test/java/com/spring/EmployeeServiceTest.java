package com.spring;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = HrSystemApplication.class)
public class EmployeeServiceTest
{
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void add_employee() throws Exception
    {
        /*
            in this test: if the adding process is success it is expected to
            receive the same sent employee object as a return from ( employeeService.addEmployee() )
         */
        Employee employeeToAdd = new Employee();
        employeeToAdd.setName("Ahmed");
        employeeToAdd.setId(101L);
        // expected to get same (employeeToAdd) object at (resultReturned)
        Employee resultReturned = employeeService.addEmployee(employeeToAdd);

        // compare the two objects
        assertEquals(resultReturned.getId(), employeeToAdd.getId());
        assertEquals(resultReturned.getName(), employeeToAdd.getName());
        assertEquals(resultReturned.getManager(), employeeToAdd.getManager());
    }

    @Test
    public void get_employee_with_id()
    {
        /*
            in this test : employee id is sent in the path params and the return expected to be
            the full employee
         */
        Long idToLookFor = 101L;
        // expected to get full employee (employeeToFind) object at (resultReturned)
        Employee resultReturned = employeeService.getEmployee(idToLookFor);
        // in case the employee was not found exception will be thrown
        assertEquals(resultReturned.getId(), idToLookFor); // compare the two objects
    }
}
