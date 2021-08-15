package com.spring.Department;

import com.spring.Employee.Employee;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Department
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy="department")
    private Set<Employee> employees;
}
