package com.spring.Employee;

import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee addEmployee(@RequestBody Employee employee) throws Exception, CustomException
    {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/")
    public List<Employee> getEmployees()
    {
        return employeeService.getEmployees();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable String id) throws CustomException // send path parameter
    {
        Employee emp = employeeService.getEmployee(Long.parseLong(id));
        if(emp == null)
            throw new CustomException("this user Id does not exits");
        return emp;
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteEmployee(@PathVariable String id) throws CustomException
    {
        return employeeService.deleteEmployee(Long.parseLong(id));
    }

//    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Employee modifyEmployee(@PathVariable String id, @RequestBody EmployeeModifyCommand employeeModifyCommand) throws NotFoundException, CustomException
//    {
//        return employeeService.modifyEmployee(Long.parseLong(id), employeeModifyCommand);
//    }

    @GetMapping(value = "/salary/{id}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeSalaryDTO getEmployeeSalary(@PathVariable String id, @PathVariable String date) throws CustomException
    {
        // (2018-12-06) date in this format
        return employeeService.employeeSalary(Long.parseLong(id), LocalDate.parse(date));
    }

    @GetMapping("name/{name}")
    public List<Employee> getEmployeesByName(@PathVariable String name)
    {
        return employeeService.getEmployeesByName(name);
    }
//    @GetMapping("manager/recursive/{id}")
//    public List<EmployeeInfoOnlyDTO> getEmployeesUnderManagerRecursively(@PathVariable String id) throws CustomException
//    {
//        return employeeService.getManagerEmployeesRecursively(Long.parseLong(id));
//    }

    @GetMapping("manager/{id}")
    public List<EmployeeInfoOnlyDTO> getEmployeesUnderManager(@PathVariable String id) throws CustomException
    {
        return employeeService.getManagerEmployees(Long.parseLong(id));
    }




}
