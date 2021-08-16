package com.spring.Department;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController
{
    @Autowired
    DepartmentService departmentService;

    @PostMapping("/")
    public Department addDepartment(@RequestBody Department department)
    {
        return departmentService.addDepartment(department);
    }
    @GetMapping("/")
    public List<Department> getDepartments()
    {
        return departmentService.getDepartments();
    }

}
