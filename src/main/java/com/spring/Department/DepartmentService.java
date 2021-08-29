package com.spring.Department;

import com.spring.ExceptionsCustom.CustomException;
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

    public Department getDepartment(Long id) throws Exception, CustomException
    {
        if(departmentRepository.findById(id).isPresent())
            return departmentRepository.findById(id).get();
        else
            throw new CustomException("This department Id does not exist");
    }

}
