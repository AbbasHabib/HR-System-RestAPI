package com.spring.Department;

import com.spring.Employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class DepartmentService
{
    @Autowired
    private DepartmentRepository departmentRepository;

    public Department addDepartment(Department department)
    {
        return departmentRepository.save(department);
    }

    @GetMapping("/view")
    public List<Department> getDepartments()
    {
        return departmentRepository.findAll();
    }

}
