package com.hrsystem.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping("/department/")
    public Department addDepartment(@RequestBody Department department) throws Exception {
        return departmentService.addDepartment(department);
    }

    @GetMapping("/department/")
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    @GetMapping("/department/{id}")
    public Department getDepartment(@PathVariable String id) throws Exception {
        return departmentService.getDepartment(Long.parseLong(id));
    }

    @GetMapping("/profile/department")
    public Department getDepartmentByLoggedUser() throws Exception {
        return departmentService.getDepartmentByLoggedUser();
    }
}
