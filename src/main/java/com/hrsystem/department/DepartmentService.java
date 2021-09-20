package com.hrsystem.department;

import com.hrsystem.employee.EmployeeService;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeService employeeService;

    public Department addDepartment(Department department) throws CustomException {
        if (department.getName() == null)
            throw new CustomException("departmentName cannot be null!");
        return departmentRepository.save(department);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartment(Long id) throws Exception {
        if (departmentRepository.findById(id).isPresent())
            return departmentRepository.findById(id).get();
        else
            throw new CustomException("This department Id does not exist");
    }

    public Department getDepartmentByLoggedUser() throws Exception {
        Department department = employeeService.getEmployeeByLoggedUser().getDepartment();
        if (department == null)
            throw new CustomException("this employee is not enrolled in a department!");
        return getDepartment(department.getId());
    }

}
