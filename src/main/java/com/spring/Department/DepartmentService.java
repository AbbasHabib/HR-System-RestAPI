package com.spring.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Department> getDepartments()
    {
        return departmentRepository.findAll();
    }

    public Department getDepartment(Long id)
    {
        return departmentRepository.findById(id).isPresent() ?
                departmentRepository.findById(id).get()
                : null;
    }

}
